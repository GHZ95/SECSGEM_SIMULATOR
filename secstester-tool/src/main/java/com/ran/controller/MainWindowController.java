package com.ran.controller;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.ran.Main;
import com.ran.aio.LinkSecsItem;
import com.ran.aio.SECSBody;
import com.ran.aio.SECSHeader;
import com.ran.aio.SECSMsg;
import com.ran.aio.SECSMsgUtil;
import com.ran.aio.Server;
import com.ran.aio.SessionType;
import com.ran.bean.MsgType;
import com.ran.cpmt.ConfigBean;
import com.ran.cpmt.OutputStreamByTextArea;
import com.ran.cpmt.SenderMenu;
import com.ran.river.NotifyBus;
import com.ran.service.MsgBridge;
import com.ran.view.SettingView;

import de.felixroske.jfxsupport.FXMLController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.util.Duration;

@FXMLController
public class MainWindowController implements Initializable {

	@FXML
	private TreeView<String> eapTreeView;

	private final static Logger logger = LoggerFactory.getLogger("recommend");

	@Autowired
	private ConfigBean configBean;
	
	@Autowired
	private MsgBridge msgBridgeImpl;
	
	@Autowired
	private Server serverHandle;

	@FXML
	private TextArea txtAreaReciver;
	
	@FXML
	private TextArea txtAreaSender;
	
	@FXML
	private TextArea txtAreaConsole;
	

	@FXML
	private RadioButton rbByType;

	@FXML
	private RadioButton rbByHost;

	@FXML
	private RadioButton rbByEQP;

	@FXML
	private ToggleGroup treeViewGroup;
	
	@FXML
	private ScrollPane sp;
	
	@FXML
	private Button btnConnect;

	@FXML
	private Label lblConnectionInfo;
	
	@Autowired
	private SECSubScriber secsSubscriber;

	@Autowired
	private Environment ev;

	public void showMainArea(String context, MsgType msgType) {
		Text text = new Text();
		switch (msgType) {
		case NORMAL: {
			text.setStyle("-fx-fill: #0000FF;");
			break;
		}
		case WARNING: {
			text.setStyle("-fx-fill: #EE7700;");
			break;
		}
		case ERROR: {
			text.setStyle("-fx-fill: #CC0000;-fx-font-weight:bold;");
			break;
		}

		default:
			break;

		}

		if (msgType == MsgType.NONE) {
			text.setText(System.lineSeparator());
		} else
			text.setText(LocalDateTime.now().toString() + " " + context + System.lineSeparator());

		txtAreaReciver.appendText(context);
		txtAreaReciver.setScrollTop(Double.MAX_VALUE);
		//sp.setVvalue(1.0);
		//slowScrollToBottom(sp);
	}

	
	public void showMainConsole(String context, MsgType msgType) {
		Text text = new Text();
		switch (msgType) {
		case NORMAL: {
			text.setStyle("-fx-fill: #0000FF;");
			break;
		}
		case WARNING: {
			text.setStyle("-fx-fill: #EE7700;");
			break;
		}
		case ERROR: {
			text.setStyle("-fx-fill: #CC0000;-fx-font-weight:bold;");
			break;
		}

		default:
			break;

		}

		if (msgType == MsgType.NONE) {
			text.setText(System.lineSeparator());
		} else
			text.setText(LocalDateTime.now().toString() + " " + context + System.lineSeparator());

		txtAreaConsole.appendText(context);
		txtAreaConsole.setScrollTop(Double.MAX_VALUE);

	}
	
	private void slowScrollToBottom(ScrollPane scrollPane) {
	    Animation animation = new Timeline(
	        new KeyFrame(Duration.seconds(2),
	            new KeyValue(scrollPane.vvalueProperty(), 1)));
	    animation.play();
	}
	
	
	


	public void showSettingView(Event event) throws IOException {
		Main.showView(SettingView.class, Modality.NONE);

	}
	
	
	public void doAutoReply(Event event) {
		
		
	}
	
	public void doConnect(Event event) {
		
		
		
		
		//Server serverHandle = Server.getInstance();
		if(!serverHandle.serverIsOpen()) {
			try {
				serverHandle.serverBoot(8878);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			btnConnect.setText("DIS");
			lblConnectionInfo.setBackground(new Background(new BackgroundFill(Color.GREEN,null,null)));
		}else {
			
			serverHandle.serverStop();
			btnConnect.setText("Connect");
			lblConnectionInfo.setBackground(new Background(new BackgroundFill(Color.GRAY,null,null)));
		}
		
		
	}

	
	

	private void secsSend(ActionEvent event) {
		// TODO Auto-generated method stub
		int from = txtAreaSender.getCaretPosition();
		//txtAreaSender.selectRange(from, to);
		String wholeStr = txtAreaSender.getText();
		String secsMsgArr[] = wholeStr.split("\\n\\n");
		boolean needReply = false;
		int beforeSize= 0 ;
		int localSize = 0;
		int targetAnchor = -1;
		for(int i =0 ; i< secsMsgArr.length;i++) {
			localSize =beforeSize+ secsMsgArr[i].length();
			if(from>=beforeSize && from<= localSize)
			{
				targetAnchor = i;
				//beforeSize = beforeSize +(2*i);
				//localSize  = localSize+(2*i);
				break;
				
			}
			beforeSize = +localSize + 2;
		} 
		if(targetAnchor!= -1) {
		//System.out.println(secsMsgArr[targetAnchor]);
		txtAreaSender.selectRange(beforeSize, localSize);
		}
		
		
		
		//parse secs msg
		String originalSecsStr = secsMsgArr[targetAnchor];
		
		//get stream function.
		String streamFunctionReg = "(\\d|S|s)(\\d+)(\\d|F|f)(\\d+ )";
	

		Pattern r = Pattern.compile(streamFunctionReg);
		Matcher m = r.matcher(originalSecsStr);
		
		if(!m.find()) {
			System.out.println("Can't find stream function.");
			return;
		}
		
		String streamFunction[] = m.group().split("F");
		int streamNo = Integer.parseInt( streamFunction[0].replace("S", "").trim());
		int functionNo = Integer.parseInt( streamFunction[1].trim());
		
		needReply = functionNo%2 ==0? false: true;
		
		Queue<String> originalQueue = SECSMsgUtil.getInstance().parseStrMsgToQueue(originalSecsStr);
		
		if(originalQueue==null) {
			System.out.println("Parse SECS message fail.");
		}else {
			SECSMsg secsMsg=new SECSMsg();
			//header
			SECSHeader header = new SECSHeader();
			header.setDeviceId(0);
			header.setFunctionNo(functionNo);
			header.setNeedReply(needReply);
			header.setpType(null);
			header.setSessionId(0);
			header.setStreamNo(streamNo);
			header.setsType(SessionType.DataMessage);
			header.setSystemByte("FFFFFFFF");
			secsMsg.setHeader(header);
			
			//body
			if(originalQueue.size()!=0) {
			SECSBody body = new SECSBody();
			Queue<String> copyQueue = new LinkedBlockingQueue<String>(originalQueue);
			body.setOriginQueue(copyQueue);
			LinkSecsItem secsBody =SECSMsgUtil.getInstance().buildSecs(originalQueue);
			body.setRootItem(secsBody);
			secsMsg.setBody(body);
			}
			
			msgBridgeImpl.sendMsg(secsMsg);
		
		}
		
	}
	
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		NotifyBus.INSTANCE.addSubscriber(secsSubscriber);
		
		txtAreaSender.setContextMenu(SenderMenu.getInstance());
		
		SenderMenu.getInstance().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if ("Send".equals(((MenuItem) event.getTarget()).getText())) {
					secsSend(event);
				}

				else {
					//secslocation(event);
				}
			}

			
		});
		
	
		PrintStream ps = new PrintStream(new OutputStreamByTextArea(txtAreaConsole));
		System.setOut(ps);
	    System.setErr(ps);
	}

}
