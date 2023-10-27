package com.ran.service;

import com.ran.aio.SECSMsg;
import com.ran.cpmt.MsgDirection;

public interface MsgBridge {

	boolean sendMsg(SECSMsg secsMsg);
	
	boolean receivedMsg(SECSMsg secsMsg);
	
	int getSysByte();
	
}
