package com.ran.aio;

public enum SessionType {
	UNDEFINED(-1), 
	DataMessage(0),
	SelectReq(1),
	SelectRsp(2),
	DeSelectReq(3),
	DeSelectRsp(4),
	LinkTestReq(5),
	LinkTestRsp(6),
	RejectReq(7),
	SeparateReq(9);

	private int code;

	SessionType(int b) {
		// TODO Auto-generated constructor stub
		this.code= b;
	}

	public static SessionType get(int b) {

		for (SessionType v : values()) {
			if (v.code == b) {
				return v;
			}
		}

		return UNDEFINED;
	}

	public int getCode() {
		return code;
	}

	
	
}
