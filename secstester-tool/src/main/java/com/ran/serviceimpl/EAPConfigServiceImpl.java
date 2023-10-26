package com.ran.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.ran.bean.EAPConfig;
import com.ran.service.EAPConfigService;

@Service
public class EAPConfigServiceImpl implements EAPConfigService {

	@Override
	@CacheEvict(cacheNames="eapCfgList",beforeInvocation = true)
	public void reLoad() {
		// TODO Auto-generated method stub
		
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	@Cacheable("eapCfgList")
	public List<EAPConfig> getAllEapCfg() {
		// TODO Auto-generated method stub
		List<EAPConfig> eapList = null;
		String queryStr= "SELECT * FROM EAP_CFG_INFO";
		try {
			eapList = new ArrayList<EAPConfig>();
			RowMapper<EAPConfig> rm = new BeanPropertyRowMapper<EAPConfig>(EAPConfig.class);
			
			eapList = jdbcTemplate.query(queryStr, rm);
			
			/*
			List<Map<String, Object>> list = jdbcTemplate.queryForList(queryStr);
			for(Map<String,Object> map : list) {
				EAPConfig ec = new EAPConfig();
				 for (Map.Entry<String, Object> entry : map.entrySet()) {
		                System.out.println(entry.getKey() + "=" + entry.getValue());
		                
		                
		            }
				 //eapList .add(ec);
			}
			*/
			
			
			
			
		}catch(Exception e) {
			eapList = null;
			
		}
		
		
		return eapList;
	}

	@Override
	public EAPConfig getEapCfgByEQPID(String eqpId) {
		// TODO Auto-generated method stub
		List<EAPConfig> innerList = getAllEapCfg();
		
		
		return innerList.stream().filter((i) -> eqpId.equals(i.getEqpId())).findFirst().get();
	}

	@Override
	public boolean addEapConfig(EAPConfig ec) {
		// TODO Auto-generated method stub
		//ec.toUpper();
		boolean rtnFlag = true;
		try {
		int r = jdbcTemplate.update("INSERT INTO EAP_CFG_INFO (eqp_id,ws_id,eqp_type,ip_local) VALUES (?, ? ,? , ?)", 
				ec.getEqpId(),ec.getWsId(),ec.getEqpType(),ec.getIpLocal());
		rtnFlag =  r < 0?false:true;
		}catch (Exception e) {
			rtnFlag = false;
			
		}
		return rtnFlag;
	}

	@Override
	public List<String> getAllHost() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateEapConfig(EAPConfig ec) {
		//ec.toUpper();
		String sql = "UPDATE EAP_CFG_INFO SET WS_ID=?,EQP_TYPE=?,IP_LOCAL=?,GUI_WS_ID=?,GUI_IP=? WHERE EQP_ID=?";
		boolean rtnFlag = true;
		try {
		int r = jdbcTemplate.update(sql, 
				ec.getWsId(),ec.getEqpType(),ec.getIpLocal(),ec.getGuiWsId(),ec.getGuiIp(),ec.getEqpId());
		rtnFlag =  r < 0?false:true;
		}catch (Exception e) {
			rtnFlag = false;
			
		}
		return rtnFlag;
	}

}
