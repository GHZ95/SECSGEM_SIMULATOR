package com.ran.controller;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;

import com.ran.bean.ConnectionMode;
import com.ran.bean.SimulatorConfig;
import com.ran.cpmt.ConfigBean;

import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

@FXMLController
public class SetController  implements Initializable{
    
	@FXML
	private TextField txtIpLocal;
	@FXML
	private TextField txtIpRemote;
	@FXML
	private TextField txtPortLocal;
	@FXML
	private TextField txtPortRemote;
	
	@FXML
	private TextField txtDeviceId;
	
	@FXML
	private TextField txtSwVersion;

	@FXML
	private TextField txtT3Timer;
	
	@FXML
	private ComboBox<String> cbxConnectionMode;
	
	@Autowired
	private ConfigBean configBean;
	

    
    public boolean setConfig(Event event) {
    
    	ConnectionMode connectionMode =ConnectionMode.valueOf( cbxConnectionMode.getValue());
    	String ipLocal = txtIpLocal.getText();
    	String ipRemote = txtIpRemote.getText();
    	String portLocal = txtPortLocal.getText();
    	String portRemote = txtPortRemote.getText();
    	String deviceId = txtDeviceId.getText();
    	String t3Timer = txtT3Timer.getText();
    	
    	SimulatorConfig config = configBean.getInnerConfig();
    	
    	config.setIpLocal(ipLocal);
    	config.setIpRemote(ipRemote);
    	config.setPortLocal(portLocal);
    	config.setPortRemote(portRemote);
    	config.setDeviceId(deviceId);
    	config.setT3Timer(t3Timer);
    	config.setConnectionMode(connectionMode);
    	
    	config.setSwVer(txtSwVersion.getText());
    	
    	configBean.setInnerConfig(config);
    	
    	Stage stage= (Stage)txtT3Timer.getScene().getRoot().getScene().getWindow();
    	stage.close();
    	return false;
    }
    
    private String getIpByHostName(String hostName) throws UnknownHostException {
    	String addr = "";
    	
			InetAddress i = InetAddress.getByName(hostName);
			addr = i.getHostAddress();
		
    	return addr;
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		  ObservableList<String> options = FXCollections.observableArrayList(
		            "ACTIVE", "PASSIVE");
		  cbxConnectionMode.setItems(options);
		        
		        // 设置默认选中项
		  cbxConnectionMode.setValue("ACTIVE");

	}
}
