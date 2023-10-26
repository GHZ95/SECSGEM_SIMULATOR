package com.ran.river;

import com.ran.aio.SECSMsg;

public class SecsEvent extends BootEvent{

	
	private String stream;
	private String function;
	
	private SECSMsg msg;
	public SecsEvent(Object source, SECSMsg msg) {
        super(source);
        this.msg = msg;
    }
	public String getStream() {
		return stream;
	}
	public void setStream(String stream) {
		this.stream = stream;
	}
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public SECSMsg getMsg() {
		return msg;
	}
	public void setMsg(SECSMsg msg) {
		this.msg = msg;
	}
	
	

}
