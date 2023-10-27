package com.ran.aio;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ran.cpmt.ConfigBean;
import com.ran.service.MsgBridge;

@Service
public class ChannelHandler implements CompletionHandler<Integer, Attachment> {

	@Autowired
	private MsgBridge msgBridgeImpl;

	@Autowired
	private ConfigBean configBean;
	
	Attachment attachment;

	@Override
	public void completed(Integer result, Attachment att) {

		SECSMsg secsMsg = new SECSMsg();
		attachment = att;
		if (att.isReadMode()) {

			ByteBuffer lenBuffer = ByteBuffer.allocate(8);
			ByteBuffer headerBuffer = ByteBuffer.allocate(10);

			((Buffer) lenBuffer).clear();
			lenBuffer.put(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0 });
			byte lenArr[] = new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0 };

			ByteBuffer buffer = att.getBuffer();
			buffer.flip();
			byte bytes[] = new byte[buffer.limit()];
			buffer.get(bytes);

			lenBuffer.put(bytes);
			lenBuffer.flip();
			long msgLen = lenBuffer.getLong();
			

			while (headerBuffer.hasRemaining())
				readChannel(att.getClient(), headerBuffer);

			((Buffer) headerBuffer).flip();
			byte[] headBytes = new byte[10];
			headerBuffer.get(headBytes);

			SECSHeader secsHeader = SECSMsgUtil.getInstance().buildSecsHeader(headBytes);

			for (byte i : headBytes) {

				System.out.print(bytes2HexString(i));
			}
			System.out.println();

	

			int bodyLen = (int) (msgLen - 10L);
			if (bodyLen > 1) {

				ByteBuffer bodyBuffer = ByteBuffer.allocate(bodyLen);
				while (bodyBuffer.hasRemaining())
					readChannel(att.getClient(), bodyBuffer);

				((Buffer) bodyBuffer).flip();
				byte[] bodyBytes = new byte[bodyLen];
				bodyBuffer.get(bodyBytes);

				SECSBody secsBody = SECSMsgUtil.getInstance().buildSecsBodyForSTR(bodyBytes);

				// secsMsg = new SECSMsg();
				secsMsg.setHeader(secsHeader);
				secsMsg.setBody(secsBody);
				secsMsg.setMsgLen((int) msgLen);

				// NotifyBus.INSTANCE.publish(new SecsEvent(this, secsMsg));

				/*
				 * byte sendArr [] = SECSMsgUtil.getInstance().pareSecsBodyToArr(secsBody,
				 * (int)msgLen);
				 * 
				 * 
				 * System.out.println("Body Receive:"+bodyBytes.length);
				 * 
				 * for (byte i : bodyBytes) {
				 * 
				 * System.out.print(bytes2HexString(i)); } System.out.println();
				 * System.out.println("Body UnSerialable:"+sendArr.length);
				 * 
				 * for (byte i : sendArr) {
				 * 
				 * System.out.print(bytes2HexString(i)); }
				 * 
				 * System.out.println();
				 * 
				 */
				// writeChannelForSend(att.getClient(),headBytes,sendArr);

				/*
				 * for (byte i : bodyBytes) {
				 * 
				 * System.out.print(bytes2HexString(i)); }
				 */

			} else {// have no body.

				// secsMsg = new SECSMsg();
				secsMsg.setHeader(secsHeader);
				secsMsg.setMsgLen((int) msgLen);

				// NotifyBus.INSTANCE.publish(new SecsEvent(this, secsMsg));
			}

			msgBridgeImpl.receivedMsg(secsMsg);

			// 读取来自客户端的数据
			/*
			 * ByteBuffer buffer = att.getBuffer(); buffer.flip(); byte bytes[] = new
			 * byte[buffer.limit()]; buffer.get(bytes);
			 */
			// String msg = new String(buffer.array()).toString().trim();
			// System.out.println("收到来自客户端的数据: " + msg);

			// 响应客户端请求，返回数据
			buffer.clear();
			// buffer.put("Response from server!".getBytes(Charset.forName("UTF-8")));
			// att.setReadMode(false);
			buffer.flip();
			// 写数据到客户端也是异步
			// att.getClient().write(buffer, att, this);

			// send response
			// writeChannel(att.getClient(),headBytes);

			att.setReadMode(true);
			att.getBuffer().clear();
			att.getClient().read(att.getBuffer(), att, this);

		} else {
			// 到这里，说明往客户端写数据也结束了，有以下两种选择:
			// 1. 继续等待客户端发送新的数据过来
			att.setReadMode(true);
			att.getBuffer().clear();
			att.getClient().read(att.getBuffer(), att, this);
			// 2. 既然服务端已经返回数据给客户端，断开这次的连接
			/*
			 * try { att.getClient().close(); } catch (IOException e) { }
			 */
		}
	}

	@Override
	public void failed(Throwable t, Attachment att) {
		System.out.println("Connction OFF.");
		
		configBean.getInnerConfig().setConnectionStatus("DIS");
	}

	public void close() {
		
		try { attachment.getClient().close(); } catch (IOException e) { }
	}
	
	public boolean writeChannel(byte[] arr) {
		boolean rtnFlag = true;
		AsynchronousSocketChannel channel = attachment.getClient();
		ByteBuffer buffer = ByteBuffer.allocate(arr.length);
		buffer.put(arr);
		((Buffer) buffer).flip();
		while (buffer.hasRemaining()) {
			Future<Integer> future = channel.write(buffer);

			try {
				int w = future.get().intValue();

				if (w <= 0) {
					System.out.println("Future =" + w);
					rtnFlag = false;
				}
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				rtnFlag = false;
			}
		}
		return rtnFlag;
	}

	public boolean writeChannelForRqstSelect(AsynchronousSocketChannel channel) {

		boolean rtnFlag = true;
		//AsynchronousSocketChannel channel = attachment.getClient();
		ByteBuffer buffer = ByteBuffer.allocate(14);
		long len = 10L;

		buffer.put((byte) (len >> 24));
		buffer.put((byte) (len >> 16));
		buffer.put((byte) (len >> 8));
		buffer.put((byte) (len));

		byte replySelect[] = new byte[10];
		

	
		replySelect[0] = (byte) 0x0;
		replySelect[1] = (byte) 0x0;
		replySelect[2] = (byte) 0x0;
		replySelect[2] = (byte) 0x0;

		replySelect[3] = (byte) 0x0;
	
		replySelect[4] = (byte) 0;
	
		replySelect[5] = (byte) 1;

		int sysInt = msgBridgeImpl.getSysByte();
		
	
		replySelect[9] = (byte) (sysInt & 0xff);
		replySelect[8] = (byte) (sysInt >> 8);
		replySelect[7] = (byte) (sysInt >> 16);
		replySelect[6] = (byte) (sysInt >> 24);

		

		buffer.put(replySelect);
		((Buffer) buffer).flip();

		System.out.println();
		while (buffer.hasRemaining()) {
			Future<Integer> future = channel.write(buffer);

			try {
				int w = future.get().intValue();

				if (w <= 0) {
					System.out.println("Future =" + w);
					rtnFlag = false;
				}
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				rtnFlag = false;
			}
		}
		return rtnFlag;
	}

	
	
	
	public boolean writeChannelForReplySelect(byte[] arr) {

		boolean rtnFlag = true;
		AsynchronousSocketChannel channel = attachment.getClient();
		ByteBuffer buffer = ByteBuffer.allocate(14);
		long len = 10L;

		buffer.put((byte) (len >> 24));
		buffer.put((byte) (len >> 16));
		buffer.put((byte) (len >> 8));
		buffer.put((byte) (len));

		byte replySelect[] = new byte[10];
		for (int i = 0; i < arr.length; i++) {

			switch (i) {
			case 2:
				replySelect[i] = (byte) 0x0;
				break;
			case 3:
				replySelect[i] = (byte) 0x0;
				break;
			case 4:
				replySelect[i] = (byte) 0;
				break;
			case 5:
				replySelect[i] = (byte) 2;
				break;

			default:
				replySelect[i] = arr[i];
				break;
			}
			System.out.print(bytes2HexString(replySelect[i]));

		}

		buffer.put(replySelect);
		((Buffer) buffer).flip();

		System.out.println();
		while (buffer.hasRemaining()) {
			Future<Integer> future = channel.write(buffer);

			try {
				int w = future.get().intValue();

				if (w <= 0) {
					System.out.println("Future =" + w);
					rtnFlag = false;
				}
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				rtnFlag = false;
			}
		}
		return rtnFlag;
	}

	public void writeChannelForSend(AsynchronousSocketChannel channel, byte[] header, byte[] body) {
		System.out.println();

		ByteBuffer buffer = ByteBuffer.allocate(14);
		long len = 10L + body.length;

		buffer.put((byte) (len >> 24));
		buffer.put((byte) (len >> 16));
		buffer.put((byte) (len >> 8));
		buffer.put((byte) (len));
		/*
		 * byte replySelect [] = new byte[10]; for( int i=0;i<header.length;i++) {
		 * 
		 * switch (i) { case 2: replySelect[i]= (byte)0x0; break; case 3:
		 * replySelect[i]= (byte)0x0; break; case 4: replySelect[i]= (byte)0 ; break;
		 * case 5: replySelect[i]= (byte)2 ; break;
		 * 
		 * default: replySelect[i]= header[i]; break; }
		 * System.out.print("SendHeader:"+bytes2HexString(replySelect[i]));
		 * 
		 * }
		 */

		buffer.put(header);
		((Buffer) buffer).flip();

		System.out.println();
		while (buffer.hasRemaining()) {
			Future<Integer> future = channel.write(buffer);

			try {
				int w = future.get().intValue();

				if (w <= 0) {
					System.out.println("Future =" + w);
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		buffer = ByteBuffer.allocate(body.length);
		buffer.put(body);
		((Buffer) buffer).flip();
		System.out.println();
		while (buffer.hasRemaining()) {
			Future<Integer> future = channel.write(buffer);

			try {
				int w = future.get().intValue();

				if (w <= 0) {
					System.out.println("Future =" + w);
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void readChannel(AsynchronousSocketChannel result, ByteBuffer headerBuffer2) {
		// TODO Auto-generated method stub
		final Future<Integer> f = result.read(headerBuffer2);

		try {
			f.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String bytes2HexString(byte b) {
		String ret = "";

		String hex = Integer.toHexString(b & 0xFF);
		if (hex.length() == 1) {
			hex = '0' + hex;
		}
		ret += hex.toUpperCase();

		return ret;
	}
}
