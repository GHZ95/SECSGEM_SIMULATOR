package com.ran.serviceimpl;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ran.aio.SECSHeader;
import com.ran.aio.SECSMsg;
import com.ran.aio.SECSMsgUtil;
import com.ran.aio.Server;
import com.ran.bean.MsgType;
import com.ran.controller.MainWindowController;
import com.ran.cpmt.MsgDirection;
import com.ran.river.NotifyBus;
import com.ran.river.SecsEvent;
import com.ran.service.MsgBridge;

@Service
public class MsgBridgeImpl implements MsgBridge {

	@Autowired
	private MainWindowController mainWindowController;

	@Autowired
	private Server serverHandle;
	
	
	private int sysByte;
	// msgmainbus

	// secsutil

	// secs io channel

	// T3 TIMEOUT QUEUE
	private Set<Integer> sendSet;

	// T6 TIMEOUT QUEUE

	@Override
	public boolean sendMsg(SECSMsg secsMsg) {
		// TODO Auto-generated method stub
		// PO /SO
		MsgDirection msgDirection = MsgDirection.SO;
		SECSHeader header = secsMsg.getHeader();

		
		// body byte
		Byte[] sendBodyArr ;
		if(secsMsg.getBody()==null) {
			sendBodyArr = new Byte[0];
		}else {
		sendBodyArr = SECSMsgUtil.getInstance().parseSecsBody(secsMsg.getBody().getOriginQueue());
		}
		// header byte
		byte[] headBytes = SECSMsgUtil.getInstance().parseHeader(header);
		if ("FFFFFFFF".equals(header.getSystemByte())) {
			sysByte = sysByte+1;
			headBytes[9] = (byte) (sysByte & 0xff);
			headBytes[8] = (byte) (sysByte >> 8);
			headBytes[7] = (byte) (sysByte >> 16);
			headBytes[6] = (byte) (sysByte >> 24);
			StringBuffer sb = new StringBuffer();
			sb.append(SECSMsgUtil.getInstance().bytes2HexString(headBytes[6]));
			sb.append(SECSMsgUtil.getInstance().bytes2HexString(headBytes[7]));
			sb.append(SECSMsgUtil.getInstance().bytes2HexString(headBytes[8]));
			sb.append(SECSMsgUtil.getInstance().bytes2HexString(headBytes[9]));
			header.setSystemByte(sb.toString());
			
			msgDirection = MsgDirection.PO;
			sendSet.add(sysByte);
		}

		// length byte
		int len = sendBodyArr.length + 10;
		byte[] lenByte = SECSMsgUtil.getInstance().int2ByteReverse(len);

		byte sendPacket[] = appenBytes(lenByte, headBytes, getbyteFromByte(sendBodyArr));
		if (!serverHandle.getChannelHandler().writeChannel(sendPacket)) {
			System.out.println(msgDirection + " Fail.");
		} else {
			String msgStr = secsMsg.toString();
			mainWindowController.showMainArea("[" + msgDirection + "]SEND-" + msgStr, MsgType.NORMAL);

		}

		return false;
	}

	@Override
	public boolean receivedMsg(SECSMsg secsMsg) {
		// TODO Auto-generated method stub
		String msgStr = secsMsg.toString();
		MsgDirection msgDirection = MsgDirection.PI;
		int sysByte =  new BigInteger(secsMsg.getHeader().getSystemByte(), 16).intValue();
		if(sendSet.contains(sysByte)) {
			
			sendSet.remove(sysByte);
			msgDirection = MsgDirection.SI;
		}
		mainWindowController.showMainArea("["+ msgDirection+"]RECEIVE-" + msgStr, MsgType.NORMAL);
		NotifyBus.INSTANCE.publish(new SecsEvent(this, secsMsg));
		return false;
	}

	public MsgBridgeImpl() {
		// TODO Auto-generated constructor stub
		sysByte = 0;
		sendSet = new HashSet<Integer>();
	}

	private byte[] getbyteFromByte(Byte[] boxByteArr) {

		
		byte tmp[] = new byte[boxByteArr.length];
		for (int i = 0; i < boxByteArr.length; i++) {
			tmp[i] = boxByteArr[i];
		}
		return tmp;
	}

	private byte[] appenBytes(byte len[], byte header[], byte body[]) {
		byte firstPart[] = new byte[14];
		System.arraycopy(len, 0, firstPart, 0, 4);
		System.arraycopy(header, 0, firstPart, 4, 10);

		if (body.length > 0) {
			byte fullPart[] = new byte[body.length + 14];
			System.arraycopy(firstPart, 0, fullPart, 0, 14);
			System.arraycopy(body, 0, fullPart, 14, body.length);

			return fullPart;

		} else {

			return firstPart;
		}

	}

}
