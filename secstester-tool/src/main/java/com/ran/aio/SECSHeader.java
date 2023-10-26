package com.ran.aio;

public class SECSHeader {

	
	private String originalStr;
	
	private byte[] binArr;
	private int sessionId;
	private int deviceId;
	private boolean needReply;
	private int streamNo;
	private int functionNo;
	private PresentionType pType;
	private SessionType sType;
	private String systemByte;
	
	public SECSHeader() {
		// TODO Auto-generated constructor stub
	}

	public String getOriginalStr() {
		return originalStr;
	}

	public void setOriginalStr(String originalStr) {
		this.originalStr = originalStr;
	}

	public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public boolean isNeedReply() {
		return needReply;
	}

	public void setNeedReply(boolean needReply) {
		this.needReply = needReply;
	}

	public int getStreamNo() {
		return streamNo;
	}

	public void setStreamNo(int streamNo) {
		this.streamNo = streamNo;
	}

	public int getFunctionNo() {
		return functionNo;
	}

	public void setFunctionNo(int functionNo) {
		this.functionNo = functionNo;
	}

	public PresentionType getpType() {
		return pType;
	}

	public void setpType(PresentionType pType) {
		this.pType = pType;
	}

	public SessionType getsType() {
		return sType;
	}

	public void setsType(SessionType sType) {
		this.sType = sType;
	}

	public String getSystemByte() {
		return systemByte;
	}

	public void setSystemByte(String systemByte) {
		this.systemByte = systemByte;
	}

	@Override
	public String toString() {
		return "S"+streamNo+"F"+functionNo+" NeedReply:"+isNeedReply()+" SystemByte:"+systemByte;
				
	}

	public byte[] getBinArr() {
		return binArr;
	}

	public void setBinArr(byte[] binArr) {
		this.binArr = binArr;
	}
	
	
	
	
}
