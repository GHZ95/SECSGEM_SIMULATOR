package com.ran.aio;

public enum SECSFormat {

	UNDEFINED(-1),
	L( 0),
	BINARY( 8),
	BOOL( 9),
	A(16),
	J(17),
	I8(24),
	I1(25),
	I2(26),
	I4(28),
	F8(32),
	F4(36),
	U8(40),
	U1(41),
	U2(42),
	U4(44);
;

private int code; 
SECSFormat(int i) {
	// TODO Auto-generated constructor stub
	this.code = i ;
}

public static SECSFormat get(int b) {

	for (SECSFormat v : values()) {
		if (v.code == b) {
			return v;
		}
	}

	return UNDEFINED;
}

public int getCode() {
	return code;
}

public void setCode(int code) {
	this.code = code;
}




}


