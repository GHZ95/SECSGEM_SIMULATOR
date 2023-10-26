package com.ran.serviceimpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ran.service.CMDTool;

@Service
public class CMDToolImpl implements CMDTool {

	private final static Logger logger = LoggerFactory.getLogger("recommend");
	
	@Autowired
	private Environment ev;
	
	
	
	private boolean runCommand(String command,boolean waitFlag) {
		boolean rtnFlag = true;
		Process ps = null;
		try {
			ps = Runtime.getRuntime().exec(command);
			/*
			 * if(ps.waitFor()!=0) { logger.error("CMD Run fail,exitValue:"+ps.exitValue());
			 * rtnFlag = false; }
			 */
		
			if(waitFlag) {
			ps.waitFor();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(ps.getInputStream())); // 获取命令输出流
			String line;
			while ((line = reader.readLine()) != null) { // 逐行读取输出结果
			logger.info(line);
			
			}
			reader.close();
			}else {
				ps.exitValue();
			}
			
			} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
			ps.destroy();
			rtnFlag= false;
		}
		ps.destroy();
		return rtnFlag;
	}
	
	
	private boolean runCommand(String command,String startPosition) {
		boolean rtnFlag = true;
		Process ps;
		try {
			ps = Runtime.getRuntime().exec(command,null,new File(startPosition));
			
			if(ps.exitValue()!=0) {
				logger.error("CMD Run fail,exitValue:"+ps.exitValue());
				rtnFlag = false;
			}	
			
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
			rtnFlag= false;
		}
		return rtnFlag;
	}
	
	@Override
	public boolean openFolder(String host, String filePath) {
		// TODO Auto-generated method stub
		logger.info("Ready to open: " + filePath);
		
		//1.clear ipc
		//2.set up ipc
		establishIPC(host);
		
		String command = "cmd /c start "+ filePath;
		logger.info(command);
		return runCommand(command,true);
		
	}
	
	

	@Override
	public boolean vncViwer(String host) {
		// TODO Auto-generated method stub
		//String command = "cmd /c java -jar tightvnc-jviewer.jar -password=%s -host=%s";
		
		String vncPassword = ev.getProperty("VNC.password");
		String command = ev.getProperty("VNC.path");
		//String command = "cmd /c %s -host=%s -password=%s";
		
		//command = String.format(command,viewerPath,host,vncPassword);
		
		command = String.format(command,host,vncPassword);
		logger.info("Ready to VNC: " + host);
		logger.info(command);
		return runCommand(command,false);
		//runCommand(command,viewerPath);
	}

	@Override
	public boolean beyondCompare(String source, String target) {
		// TODO Auto-generated method stub
		
		return false;
		
	}

	@Override
	public boolean establishIPC(String ip) {
		// TODO Auto-generated method stub
		
		clearIPC(ip);

		/* ipc is null connection ,change it to d$.
		String commandArr[] = {"cmd /c","net use","\\\\"+ip+"\\ipc$",
				ev.getProperty("IPC.password"),
				"/user:"+ev.getProperty("IPC.user")};
		*/
		String commandArr[] = {"cmd /c","net use","\\\\"+ip+"\\d$",
				ev.getProperty("IPC.password"),
				"/user:"+ev.getProperty("IPC.user")};
		
		String command = StringUtils.arrayToDelimitedString(commandArr, " ");
		logger.info("Ready to start remote explorer:" + ip);
		logger.info(command);
		return  runCommand(command,true);
	}

	@Override
	public boolean clearIPC(String ip) {
		// TODO Auto-generated method stub
		String command = "cmd /c net use * /delete /Y";
		logger.info("Clear all previous IPC connections." );
		logger.info(command);
		return  runCommand(command,true);
		
	}


	@Override
	public boolean openFolderNotByIPC(String filePath) {
		// TODO Auto-generated method stub
		String command = "cmd /c start "+ filePath;
		logger.info(command);
		return runCommand(command,true);
	}

}
