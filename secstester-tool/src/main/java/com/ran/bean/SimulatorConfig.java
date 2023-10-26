package com.ran.bean;

import org.springframework.stereotype.Component;

@Component
public class SimulatorConfig {

	private String ipLocal;
	private String ipRemote;
	private String portLocal;
	private String portRemote;
	private String deviceId;
	private String t3Timer;
	
	private ConnectionMode connectionMode;
	
	public SimulatorConfig() {
		// TODO Auto-generated constructor stub
		ipLocal = "127.0.0.1";
		ipRemote="127.0.0.1";
		portLocal = "5000";
		portRemote = "6000";
		deviceId = "0";
		t3Timer = "45";
		connectionMode = ConnectionMode.ACTIVE;
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

	public String getPortLocal() {
		return portLocal;
	}

	public void setPortLocal(String portLocal) {
		this.portLocal = portLocal;
	}

	public String getPortRemote() {
		return portRemote;
	}

	public void setPortRemote(String portRemote) {
		this.portRemote = portRemote;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getT3Timer() {
		return t3Timer;
	}

	public void setT3Timer(String t3Timer) {
		this.t3Timer = t3Timer;
	}

	public ConnectionMode getConnectionMode() {
		return connectionMode;
	}

	public void setConnectionMode(ConnectionMode connectionMode) {
		this.connectionMode = connectionMode;
	}
	
	
}
