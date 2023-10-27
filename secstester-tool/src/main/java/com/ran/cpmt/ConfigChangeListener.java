package com.ran.cpmt;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ran.controller.MainWindowController;

@Component
public class ConfigChangeListener implements PropertyChangeListener {

	@Autowired
	private MainWindowController mainWindowController;
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		// TODO Auto-generated method stub

		String propertyName = event.getPropertyName();
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();
		
		switch (propertyName) {
		// CONNECTION STATUS
		case "connectionStatus":
			if("DIS".equals(newValue)) {
				mainWindowController.changeConnectStatus("DIS");
			}else {
				mainWindowController.changeConnectStatus("CONN");
			}
			break;
		case "ipLocal":
		case "ipRemote":
		case "portLocal":
		case "portRemote":
			mainWindowController.changeIpStr();
			break;
		default:
			break;
		}

	}

}
