package com.ran.service;

public interface SECSHandler {

	public boolean openPassive(String ipLocal,String portLocal);
	
	
	public boolean openActive(String ipRemote,String portRemote);
	
	
	public void messageRecive();
	
	
}
