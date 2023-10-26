package com.ran.cpmt;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class OutputStreamByTextArea extends OutputStream {

	   private TextArea console;

       public OutputStreamByTextArea(TextArea console) {
           this.console = console;
       }

       public void appendText(String valueOf) {
           Platform.runLater(() -> {
          
           Text text = new Text();
           text.setText(LocalDateTime.now().toString() + " " + valueOf + System.lineSeparator());		   
           //console.appendText(valueOf)
           console.appendText(valueOf);
           console.setScrollTop(Double.MAX_VALUE);
           });
       }

       public void write(int b) throws IOException {
           appendText(String.valueOf((char)b));
       }

}
