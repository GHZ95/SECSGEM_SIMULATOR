package com.ran;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import com.ran.view.MainWindowView;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

@EnableCaching
@SpringBootApplication
public class Main extends AbstractJavaFxApplicationSupport{



	public static void main(String[] args) {
	  

		launch(Main.class, MainWindowView.class, args);
		
		
		
    
        //StyleManager.getInstance().addUserAgentStylesheet(Main.class.getResource("/com/only/common/css/Only.css").toString());
    }
	
	
    

    
}
