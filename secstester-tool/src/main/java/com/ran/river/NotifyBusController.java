package com.ran.river;

import java.util.Scanner;

public class NotifyBusController {

    public String notify(String msg) {
    	
    	
        //NotifyBus.INSTANCE.publish(new SecsEvent(this, msg));
        return "success";
    }
    
    /*
    public static void main(String[] args) {
    	Subscriber<SecsEvent> secsScriber = new SECSubScriber();    	
    	NotifyBus.INSTANCE.addSubscriber(secsScriber);
    	NotifyBusController nbc = new NotifyBusController();
    	
    	Scanner scanner = new Scanner(System.in);
    	while(true) {
    		
    		String str = scanner.nextLine();
    		nbc.notify(str);
    	}
    	
	}*/
    
    
}