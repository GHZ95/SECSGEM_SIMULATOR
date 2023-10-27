package com.ran.bean;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.springframework.stereotype.Component;

@Component
public class SimulatorConfig {

	private String ipLocal;
	private String ipRemote;
	private String portLocal;
	private String portRemote;
	private String deviceId;
	private String t3Timer;
	private String MDLN;
	private String swVer;
	private String connectionStatus;
	
	private PropertyChangeSupport propertyChangeSupport;
	
	
	
	private ConnectionMode connectionMode;
	
	public SimulatorConfig() {
		// TODO Auto-generated constructor stub
		ipLocal = "127.0.0.1";
		ipRemote="127.0.0.1";
		portLocal = "5000";
		portRemote = "6000";
		deviceId = "0";
		t3Timer = "45";
		MDLN = "R.A.N";
		swVer = "1.0";
		connectionMode = ConnectionMode.ACTIVE;
		connectionStatus = "DIS";
		propertyChangeSupport = new PropertyChangeSupport(this);
		
		
	}

	public String getIpLocal() {
		return ipLocal;
	}
	
	  public void addPropertyChangeListener(PropertyChangeListener listener) {
	        this.propertyChangeSupport.addPropertyChangeListener(listener);
	    }

	    public void removePropertyChangeListener(PropertyChangeListener listener) {
	        this.propertyChangeSupport.removePropertyChangeListener(listener);
	    }

	public String getConnectionStatus() {
		return connectionStatus;
	}

	public void setConnectionStatus(String connectionStatus) {
		String oldValue = this.connectionStatus;
		this.connectionStatus = connectionStatus;
		
		propertyChangeSupport.firePropertyChange("connectionStatus", oldValue, connectionStatus);
	}

	public PropertyChangeSupport getPropertyChangeSupport() {
		return propertyChangeSupport;
	}

	public void setPropertyChangeSupport(PropertyChangeSupport propertyChangeSupport) {
		this.propertyChangeSupport = propertyChangeSupport;
	}

	public String getMDLN() {
		return MDLN;
	}

	public void setMDLN(String mDLN) {
		MDLN = mDLN;
	}

	public String getSwVer() {
		return swVer;
	}

	public void setSwVer(String swVer) {
		this.swVer = swVer;
	}

	public void setIpLocal(String ipLocal) {
		String oldValue = this.ipLocal;
		this.ipLocal = ipLocal;
		propertyChangeSupport.firePropertyChange("ipLocal", oldValue, ipLocal);
	}

	public String getIpRemote() {
		return ipRemote;
	}

	public void setIpRemote(String ipRemote) {
		String oldValue = this.ipRemote;
		this.ipRemote = ipRemote;
		propertyChangeSupport.firePropertyChange("ipRemote", oldValue, ipRemote);
	}

	public String getPortLocal() {
		return portLocal;
	}

	public void setPortLocal(String portLocal) {
		String oldValue = this.portLocal;
		this.portLocal = portLocal;
		propertyChangeSupport.firePropertyChange("portLocal", oldValue, portLocal);
	}

	public String getPortRemote() {
		return portRemote;
	}

	public void setPortRemote(String portRemote) {
		String oldValue = this.portRemote;
		this.portRemote = portRemote;
		propertyChangeSupport.firePropertyChange("portRemote", oldValue, portRemote);
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
