package com.ran.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ran.aio.SECSHeader;
import com.ran.aio.SECSMsg;
import com.ran.aio.Server;
import com.ran.aio.SessionType;
import com.ran.cpmt.ConfigBean;
import com.ran.river.BootEvent;
import com.ran.river.SecsEvent;
import com.ran.river.Subscriber;
import com.ran.service.EventService;

@Service
public class SECSubScriber  extends Subscriber<SecsEvent>   {


	@Autowired
	private Server serverHandle;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private ConfigBean configBean;

	
	@Override
	public Class<? extends BootEvent> supportedType() {
		// TODO Auto-generated method stub
		return SecsEvent.class;
	}

	@Override
	public void onEvent(SecsEvent event) {
		// TODO Auto-generated method stub
		SECSMsg secsMsg = event.getMsg();
	
		//System.out.println();
		
		
		
		SECSHeader header = secsMsg.getHeader();
		boolean needReply = header.isNeedReply();
		
		SessionType sessionType = header.getsType();
		
		switch(sessionType) {
		
		case SelectReq:
			sendSelectRsp(event);
			break;
			
		case SelectRsp:
			//notiry ui show connect
			configBean.getInnerConfig().setConnectionStatus("CONN");
			
			break;
		case DataMessage:
			dataMessageHandle(event);
			break;
			
		case LinkTestReq:
			sendLinkTestRsp(event);
			break;
			
		case LinkTestRsp:
			
			break;
			default:
				break;
		}
	
		
	}

	private void sendSelectRsp(SecsEvent event) {
		// TODO Auto-generated method stub
		//Server.getInstance().getChannelHandler().writeChannelForTest(event.getMsg().getHeader().getBinArr());
		boolean rtnFlag = serverHandle.getChannelHandler().writeChannelForReplySelect(
				event.getMsg().getHeader().getBinArr());
		if(rtnFlag)
			configBean.getInnerConfig().setConnectionStatus("CONN");
	}
	
	private void sendLinkTestRsp(SecsEvent event) {
		// TODO Auto-generated method stub
		//Server.getInstance().getChannelHandler().writeChannelForTest(event.getMsg().getHeader().getBinArr());
		boolean rtnFlag = serverHandle.getChannelHandler().writeChannelForReplyLinkTest(
				event.getMsg().getHeader().getBinArr());

	}

	private void dataMessageHandle(SecsEvent event) {
		// TODO Auto-generated method stub
		SECSHeader header = event.getMsg().getHeader();
		switch(header.getStreamNo()) {
		case 1:
			switch (header.getFunctionNo()) {
			case 1:
				eventService.handleS1F1(event);
				break;
			case 13:
				eventService.handleS1F13(event);
				break;
				default:
					break;
			}
			break;
		case 2:
			break;
		case 3:
			break;
		case 6:
			switch (header.getFunctionNo()) {
			case 11:
				eventService.handleS6F11(event);
				break;
				default:
					break;
			}
			
			break;
		case 7:
			break;
			default:
				break;
		
		}
		
	}

}
