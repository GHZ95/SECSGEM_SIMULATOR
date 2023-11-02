package com.ran.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GlobalExceptionAdvice {
	
	private final static Logger logger = LoggerFactory.getLogger("recommend");

	public void handleException(Exception e ) {
		logger.error(e.getLocalizedMessage());
	}
	
}
