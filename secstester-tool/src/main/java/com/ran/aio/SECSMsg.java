package com.ran.aio;

import java.time.LocalDateTime;

public class SECSMsg {

	private int msgLen;
	private SECSHeader header;
	private SECSBody body;
	
	public SECSMsg() {
		// TODO Auto-generated constructor stub
	}

	public int getMsgLen() {
		return msgLen;
	}

	public void setMsgLen(int msgLen) {
		this.msgLen = msgLen;
	}

	public SECSHeader getHeader() {
		return header;
	}

	public void setHeader(SECSHeader header) {
		this.header = header;
	}

	public SECSBody getBody() {
		return body;
	}

	public void setBody(SECSBody body) {
		this.body = body;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		//time
		buffer.append(LocalDateTime.now().toString());
		
		buffer.append(System.getProperty("line.separator"));
		//header
		buffer.append(header.toString());
		buffer.append(System.getProperty("line.separator"));
		//body
		if(body!=null) {
		buffer.append(body.toString());
		buffer.append(System.getProperty("line.separator"));
		}else {
		buffer.append(System.getProperty("line.separator"));	
		}
		return buffer.toString();
	}
	
	
	
	
}
