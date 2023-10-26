package com.ran.service;

import java.util.List;

import com.ran.bean.EAPConfig;

public interface EAPConfigService {

	public List<EAPConfig> getAllEapCfg();
	
	public EAPConfig getEapCfgByEQPID(String eqpId);
	
	public void reLoad();
	
	public boolean addEapConfig(EAPConfig ec);
	
	public List<String> getAllHost();
	
	public boolean updateEapConfig(EAPConfig ec);
}
