package com.ran.cpmt;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class GlobalMenu extends ContextMenu {
	/** * 单例 */
	private static GlobalMenu INSTANCE = null;

	/** * 私有构造函数 */
	
	private String eqpId;

	private GlobalMenu() {
		
		MenuItem updateMenuItem = new MenuItem("VNC Server");
		MenuItem vncMenuItem = new MenuItem("VNC GUI");
		
		MenuItem settingMenuItem = new MenuItem("Open Folder");
		
		getItems().add(updateMenuItem);
		getItems().add(vncMenuItem);
		getItems().add(settingMenuItem);
		/*
		this.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println(eqpId + "right gets consumed so this must be left on "+
                        ((MenuItem)event.getTarget()).getText());
            }
        });
*/
	}
	
	/** * 获取实例 * @return GlobalMenu */

	public static GlobalMenu getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new GlobalMenu();
		}
		return INSTANCE;
	}

	public String getEqpId() {
		return eqpId;
	}

	public void setEqpId(String eqpId) {
		this.eqpId = eqpId;
	}
	
	
	
	
}
