package com.ran.service;

import com.ran.river.SecsEvent;

public interface EventService {
	
	boolean handleS1F1(SecsEvent event);
	
	boolean handleS1F3(SecsEvent event);
	
	boolean handleS1F13(SecsEvent event);

	boolean handleS6F11(SecsEvent event);
}
