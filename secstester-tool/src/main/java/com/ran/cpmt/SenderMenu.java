package com.ran.cpmt;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class SenderMenu extends ContextMenu {
	/** * 单例 */
	private static SenderMenu INSTANCE = null;

	/** * 私有构造函数 */
	
	private String eqpId;

	private SenderMenu() {
		
		MenuItem sendMenuItem = new MenuItem("Send");
		MenuItem slMenuItem = new MenuItem("Show location");

		
		getItems().add(sendMenuItem);
		getItems().add(slMenuItem);
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

	public static SenderMenu getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SenderMenu();
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
