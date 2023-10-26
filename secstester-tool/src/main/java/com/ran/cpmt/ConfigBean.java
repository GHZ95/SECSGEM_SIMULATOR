package com.ran.cpmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ran.bean.SimulatorConfig;

@Component
public class ConfigBean {

	@Autowired
	private SimulatorConfig innerConfig;

	public SimulatorConfig getInnerConfig() {
		return innerConfig;
	}

	public void setInnerConfig(SimulatorConfig innerConfig) {
		this.innerConfig = innerConfig;
	}
	
	
}
