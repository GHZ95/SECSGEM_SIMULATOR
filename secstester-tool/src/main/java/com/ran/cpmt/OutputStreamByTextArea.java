package com.ran.cpmt;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class OutputStreamByTextArea extends OutputStream {

	   private TextArea console;
	   ScheduledExecutorService scheduler;
	   int initialDelay;
	   int period;
	   private StringBuffer mainStringBuffer;
		
		private int preCount ;
		
		private final int MAX_LINES = 100 ;
		
		
       public OutputStreamByTextArea(TextArea console) {
           this.console = console;
           mainStringBuffer = new StringBuffer();
       scheduler = Executors.newScheduledThreadPool(1);
   		 initialDelay = 0; 
   		period = 100;  
   		preCount = 0;
   		scheduler.scheduleAtFixedRate(()->{
   			if(mainStringBuffer.length()!=preCount) {
				preCount = mainStringBuffer.length();
				 Pattern newline = Pattern.compile("\n");
				 Matcher matcher = newline.matcher(mainStringBuffer);
		         int lines = 1 ;
		         while (matcher.find()) lines++;
				if(lines>MAX_LINES) {
					int linesToDrop = lines - MAX_LINES ;
		            int index = 0 ; 
		            for (int i = 0 ; i < linesToDrop ; i++) {
		                //index = mainStringBuffer.indexOf(System.lineSeparator(), index) ;
		            	index = mainStringBuffer.indexOf("\r", index+1) ;
		            }
		            mainStringBuffer = mainStringBuffer.delete(0, index);
				}
				Platform.runLater(() -> {
					console.setText(mainStringBuffer.toString());
					console.setScrollTop(Double.MAX_VALUE);

				});
   			}
   		}, initialDelay, period, TimeUnit.MILLISECONDS); 
   		
       }

       public void appendText(String valueOf) {
    	   
    	   
    	   mainStringBuffer.append(valueOf);
    	   
    	   /*
           Platform.runLater(() -> {
          
           //Text text = new Text();
           //text.setText(LocalDateTime.now().toString() + " " + valueOf + System.lineSeparator());		   
           //console.appendText(valueOf)
           console.appendText(valueOf);
           console.setScrollTop(Double.MAX_VALUE);
           });
           */
       }

       public void write(int b) throws IOException {
           appendText(String.valueOf((char)b));
       }

}
