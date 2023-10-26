package com.ran.cpmt;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class HostMenu extends ContextMenu {
	/** * 单例 */
	private static HostMenu INSTANCE = null;

	/** * 私有构造函数 */
	
	private String hostId;

	private HostMenu() {
		

		MenuItem vncMenuItem = new MenuItem("VNC");
		MenuItem settingMenuItem = new MenuItem("Open Folder");
		
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

	public static HostMenu getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new HostMenu();
		}
		return INSTANCE;
	}



	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}


	
	
	
}
