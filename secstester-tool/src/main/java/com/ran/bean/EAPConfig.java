package com.ran.bean;

public class EAPConfig {

	private String eqpId;
	private String area;
	private String eqpType;
	private String wsId;
	private String guiWsId;
	private String guiIp;
	private String ipLocal;
	private String ipRemote;
	private String eqsVer;
	private String eqpVer;
	private String updateTime;
	
	public EAPConfig() {
		// TODO Auto-generated constructor stub
	}

	
	
	public EAPConfig(String eqpId, String area, String eqpType, String wsId) {
		super();
		this.eqpId = eqpId;
		this.area = area;
		this.eqpType = eqpType;
		this.wsId = wsId;
	}



	
	
	
	public String getGuiIp() {
		return guiIp;
	}



	public void setGuiIp(String guiIp) {
		this.guiIp = guiIp;
	}



	public String getGuiWsId() {
		return guiWsId==null?guiWsId:guiWsId.toUpperCase();
	}



	public void setGuiWsId(String guiWsId) {
		this.guiWsId = guiWsId;
	}



	public String getIpLocal() {
		return ipLocal;
	}



	public void setIpLocal(String ipLocal) {
		this.ipLocal = ipLocal;
	}



	public String getIpRemote() {
		return ipRemote;
	}



	public void setIpRemote(String ipRemote) {
		this.ipRemote = ipRemote;
	}



	public String getEqsVer() {
		return eqsVer;
	}



	public void setEqsVer(String eqsVer) {
		this.eqsVer = eqsVer;
	}



	public String getEqpVer() {
		return eqpVer;
	}



	public void setEqpVer(String eqpVer) {
		this.eqpVer = eqpVer;
	}



	


	public String getUpdateTime() {
		return updateTime;
	}



	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}



	public String getEqpId() {
		return eqpId==null?eqpId:eqpId.toUpperCase();
	}

	public void setEqpId(String eqpId) {
		this.eqpId = eqpId;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getEqpType() {
		return eqpType==null?eqpType:eqpType.toUpperCase();
	}

	public void setEqpType(String eqpType) {
		this.eqpType = eqpType;
	}

	public String getWsId() {
		return wsId==null?wsId:wsId.toUpperCase();
	}

	public void setWsId(String wsId) {
		this.wsId = wsId;
	}



	@Override
	public String toString() {
		return "EAPConfig [eqpId=" + eqpId + ", area=" + area + ", eqpType=" + eqpType + ", wsId=" + wsId + "]";
	}
	
	
}
