package com.ran.service;

public interface CMDTool {

	public boolean openFolder(String host,String filePath);
	
	
	public boolean vncViwer(String host);
	
	public boolean openFolderNotByIPC(String filePath);
	
	public boolean beyondCompare(String source,String target);
	
	
	public boolean establishIPC(String ip);
	
	
	public boolean clearIPC(String ip);
	
	
}
