package com.ran.river;

public class LogEvent extends BootEvent{
    private String msg;


    public LogEvent(Object source, String msg) {
        super(source);
        this.msg = msg;
    }


	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}
    
    
}
