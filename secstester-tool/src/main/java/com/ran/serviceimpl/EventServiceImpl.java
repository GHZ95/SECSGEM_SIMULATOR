package com.ran.serviceimpl;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ran.aio.LinkSecsItem;
import com.ran.aio.SECSBody;
import com.ran.aio.SECSHeader;
import com.ran.aio.SECSMsg;
import com.ran.aio.SECSMsgUtil;
import com.ran.aio.Server;
import com.ran.cpmt.ConfigBean;
import com.ran.river.SecsEvent;
import com.ran.service.EventService;
import com.ran.service.MsgBridge;

@Service
public class EventServiceImpl implements EventService{

	@Autowired
	private ConfigBean configBean;
	
	@Autowired
	private MsgBridge msgBridgeImpl;
	
	@Override
	public boolean handleS1F1(SecsEvent event) {
		// TODO Auto-generated method stub
		Queue<String> strQueue = new LinkedBlockingQueue<String>();
		strQueue.add("List:2");
		strQueue.add("A["+configBean.getInnerConfig().getMDLN().length()+"]:"+configBean.getInnerConfig().getMDLN());
		strQueue.add("A["+configBean.getInnerConfig().getSwVer().length()+"]:"+configBean.getInnerConfig().getSwVer());
		SECSMsg secsMsg = new SECSMsg();
		SECSBody body = new SECSBody();
		SECSHeader header = buildHeader(event);
		
		Queue<String> copyQueue = new LinkedBlockingQueue<String>(strQueue);
		body.setOriginQueue(copyQueue);

		LinkSecsItem secsBody =SECSMsgUtil.getInstance().buildSecs(strQueue);
		body.setRootItem(secsBody);
		secsMsg.setBody(body);
		secsMsg.setHeader(header);

		
		msgBridgeImpl.sendMsg(secsMsg);
		return false;
	}

	@Override
	public boolean handleS1F3(SecsEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean handleS1F13(SecsEvent event) {
		// TODO Auto-generated method stub
		String bodyStr=
				 "<L[2]"
				+ "	<B 00>"
				+ "	<L[2]"
				+ "		<A[8] \"IvoryTsg\">\r\n"
				+ "		<A[3] \"1.0\">\r\n"
				+ "	>"
				+ ">";

		Queue<String> strQueue = new LinkedBlockingQueue<String>();
		strQueue.add("List:2");
		strQueue.add("BINARY[1]:00");
		strQueue.add("List:2");
		strQueue.add("A["+configBean.getInnerConfig().getMDLN().length()+"]:"+configBean.getInnerConfig().getMDLN());
		strQueue.add("A["+configBean.getInnerConfig().getSwVer().length()+"]:"+configBean.getInnerConfig().getSwVer());
		SECSMsg secsMsg = new SECSMsg();
		SECSBody body = new SECSBody();
		SECSHeader header = buildHeader(event);
		
		Queue<String> copyQueue = new LinkedBlockingQueue<String>(strQueue);
		body.setOriginQueue(copyQueue);

		LinkSecsItem secsBody =SECSMsgUtil.getInstance().buildSecs(strQueue);
		body.setRootItem(secsBody);
		secsMsg.setBody(body);
		secsMsg.setHeader(header);

		
		msgBridgeImpl.sendMsg(secsMsg);
		/*
		//body byte
		Byte[] sendBodyArr = SECSMsgUtil.getInstance().parseSecsBody(strQueue);
		//header byte 
		byte[] headBytes = SECSMsgUtil.getInstance().parseHeader(header);
		
		//length byte
		int len = sendBodyArr.length + 10;
		byte[] lenByte = SECSMsgUtil.getInstance().int2ByteReverse(len);
		

		byte sendPacket[] =appenBytes(lenByte,headBytes,getbyteFromByte(sendBodyArr)) ;
		
		Server.getInstance().getChannelHandler().writeChannel(sendPacket);
		*/
		return false;
	}
	
	
	private SECSHeader buildHeader(SecsEvent event) {
		SECSHeader header = new SECSHeader();
		if(event.getMsg()!=null) {//reply
			SECSMsg receive = event.getMsg();
			SECSHeader receiveHeader = receive.getHeader();
			header.setDeviceId(receiveHeader.getDeviceId());
			header.setFunctionNo(receiveHeader.getFunctionNo() + 1);
			header.setNeedReply(false);
			header.setpType(receiveHeader.getpType());
			header.setSessionId(receiveHeader.getSessionId());
			header.setStreamNo(receiveHeader.getStreamNo());
			header.setsType(receiveHeader.getsType());
			header.setSystemByte(receiveHeader.getSystemByte());
			
		}else{//send
			
			
		}
		
		
		
		return header;
		
	}

	@Override
	public boolean handleS6F11(SecsEvent event) {
		// TODO Auto-generated method stub
		Queue<String> strQueue = new LinkedBlockingQueue<String>();
		strQueue.add("BOOL:0");
		SECSMsg secsMsg = new SECSMsg();
		SECSBody body = new SECSBody();
		SECSHeader header = buildHeader(event);
		
		Queue<String> copyQueue = new LinkedBlockingQueue<String>(strQueue);
		body.setOriginQueue(copyQueue);

		LinkSecsItem secsBody =SECSMsgUtil.getInstance().buildSecs(strQueue);
		body.setRootItem(secsBody);
		secsMsg.setBody(body);
		secsMsg.setHeader(header);

		
		msgBridgeImpl.sendMsg(secsMsg);
		
		
		return false;
	}

}
