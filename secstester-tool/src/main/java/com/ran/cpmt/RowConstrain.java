package com.ran.cpmt;
import java.util.function.UnaryOperator;

import javafx.scene.control.TextFormatter.Change;

public class RowConstrain implements UnaryOperator<Change>{

	@Override
	public Change apply(Change change) {
		// TODO Auto-generated method stub
		  String newText = change.getControlNewText();
		  //Pattern newline = Pattern.compile("\n");
          // count lines in proposed new text:
         //Matcher matcher = newline.matcher(newText);
          int lines = 1 ;
          
          //while (matcher.find()) lines++;

          lines = newText.split("\n").length;
          
          // if there aren't too many lines just return the changed unmodified:
          if (lines <= 1000) return change ;

          // drop first (lines - 50) lines and replace all text
          // (there's no other way AFAIK to drop text at the beginning 
          // and replace it at the end):
          int linesToDrop = lines - 1000 ;
          int index = 0 ; 
          for (int i = 0 ; i < linesToDrop ; i++) {
              index = newText.indexOf('\n', index) ;
          }
          change.setRange(0, change.getControlText().length());
          change.setText(newText.substring(index+1));

          newText = null;
          return change ;
	}





}
