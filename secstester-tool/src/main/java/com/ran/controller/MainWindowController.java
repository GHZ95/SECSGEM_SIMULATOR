package com.ran.controller;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.ran.Main;
import com.ran.aio.Client;
import com.ran.aio.LinkSecsItem;
import com.ran.aio.SECSBody;
import com.ran.aio.SECSHeader;
import com.ran.aio.SECSMsg;
import com.ran.aio.SECSMsgUtil;
import com.ran.aio.Server;
import com.ran.aio.SessionType;
import com.ran.bean.ConnectionMode;
import com.ran.bean.MsgType;
import com.ran.cpmt.ConfigBean;
import com.ran.cpmt.ConfigChangeListener;
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
import javafx.application.Platform;
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
	
    private final int MAX_LINES = 1000 ;

	@Autowired
	private ConfigBean configBean;

	@Autowired
	private MsgBridge msgBridgeImpl;

	@Autowired
	private Server serverHandle;

	@Autowired
	private Client clientHandle;

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
	private ConfigChangeListener lister;

	@Autowired
	private Environment ev;
	
	private StringBuffer mainStringBuffer;
	
	private int preCount ;

	public void showMainArea(String context, MsgType msgType) {
		//Text text = new Text();
		/*
		Platform.runLater(() -> {

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String tmp = txtAreaReciver.getText().intern();
			String arr[] = tmp.split("\n");
			
			if(arr.length>1000) {
		
					txtAreaReciver.clear();
					//txtAreaReciver.sett(tmp.substring(tmp.indexOf("\n")+1));
					txtAreaReciver.setText(tmp.substring(tmp.indexOf("\n")+1).intern());
			}
			
			arr = null;tmp=null;
		txtAreaReciver.appendText(LocalDateTime.now().toString() + " " + context + System.lineSeparator());

		txtAreaReciver.setScrollTop(Double.MAX_VALUE);

		
		});
		*/
		mainStringBuffer.append(LocalDateTime.now().toString() + " " + context + System.lineSeparator());
		
	
		// sp.setVvalue(1.0);
		// slowScrollToBottom(sp);
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
				new KeyFrame(Duration.seconds(2), new KeyValue(scrollPane.vvalueProperty(), 1)));
		animation.play();
	}

	public void showSettingView(Event event) throws IOException {
		Main.showView(SettingView.class, Modality.WINDOW_MODAL);

	}

	public void doAutoReply(Event event) {

	}

	public void doDisConnect(ConnectionMode mode) {
		if (mode == ConnectionMode.PASSIVE) {
			if(serverHandle.serverStop()) {
				btnConnect.setText("Connect");
				lblConnectionInfo.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
			}
			
			}else {
				if(clientHandle.clientClose()) {
					btnConnect.setText("Connect");
					lblConnectionInfo.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
				}
				
			}
	}
	
	public void doConnect(ConnectionMode mode) {
		try {
		if (mode == ConnectionMode.PASSIVE) {
			if (!serverHandle.serverIsOpen())
				serverHandle.serverBoot(Integer.parseInt(configBean.getInnerConfig().getPortLocal()));

		}else {
			
				clientHandle.clientBoot(configBean.getInnerConfig().getIpRemote(),
						Integer.parseInt(configBean.getInnerConfig().getPortRemote()));

		}
		btnConnect.setText("WAIT");
		lblConnectionInfo.setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null)));
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Operation fail.");
		}
	}
	
	
	public void btnClose(Event event) {
		
		String btnText = btnConnect.getText();
		
		ConnectionMode mode = configBean.getInnerConfig().getConnectionMode();
		doDisConnect(mode);
	}
	
	public void btnConnect(Event event) {
		//TEXT DisConnect WAIT Connect
		String btnText = btnConnect.getText();
		
		ConnectionMode mode = configBean.getInnerConfig().getConnectionMode();
		//doConnect(mode);
		// Server serverHandle = Server.getInstance();
		
		switch(btnText) {
		case "DisConnect":
			doDisConnect(mode);
			break;
		case "WAIT":
			//just wait - -.
			doDisConnect(mode);
			btnConnect.setText("Connect");
			lblConnectionInfo.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
			break;
		case "Connect":
			doConnect(mode);
			break;
			
			default:break;
		
		}
		
		/*
			try {
				
				if (mode == ConnectionMode.PASSIVE) {
					// passive
					if (!serverHandle.serverIsOpen())
					serverHandle.serverBoot(Integer.parseInt(configBean.getInnerConfig().getPortLocal()));

					
				} else {// active
					if (!clientHandle.clientIsOpen())
					clientHandle.clientBoot(configBean.getInnerConfig().getIpRemote(),
							Integer.parseInt(configBean.getInnerConfig().getPortRemote()));

				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			btnConnect.setText("WAIT");
			lblConnectionInfo.setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null)));
		} else {

			serverHandle.serverStop();
			btnConnect.setText("Connect");
			lblConnectionInfo.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
		}
*/
	}

	public void changeConnectStatus(String mark) {
		Platform.runLater(() -> {

			// use java fx thread to update....

			if ("DIS".equals(mark)) {
				btnConnect.setText("Connect");
				lblConnectionInfo.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));

			} else {
				btnConnect.setText("DisConnect");

				lblConnectionInfo.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
			}

		});
	}

	private void secsSend(ActionEvent event) {
		// TODO Auto-generated method stub
		try {
		int from = txtAreaSender.getCaretPosition();
		// txtAreaSender.selectRange(from, to);
		String wholeStr = txtAreaSender.getText();
		String secsMsgArr[] = wholeStr.split("\\n\\n");
		boolean needReply = false;
		int beforeSize = 0;
		int localSize = 0;
		int targetAnchor = -1;
		for (int i = 0; i < secsMsgArr.length; i++) {
			localSize = beforeSize + secsMsgArr[i].length();
			if (from >= beforeSize && from <= localSize) {
				targetAnchor = i;
				// beforeSize = beforeSize +(2*i);
				// localSize = localSize+(2*i);
				break;

			}
			beforeSize = +localSize + 2;
		}
		if (targetAnchor != -1) {
			// System.out.println(secsMsgArr[targetAnchor]);
			txtAreaSender.selectRange(beforeSize, localSize);
		}

		// parse secs msg
		String originalSecsStr = secsMsgArr[targetAnchor];

		// get stream function.
		String streamFunctionReg = "(\\d|S|s)(\\d+)(\\d|F|f)(\\d+ )";

		Pattern r = Pattern.compile(streamFunctionReg);
		Matcher m = r.matcher(originalSecsStr);

		if (!m.find()) {
			System.out.println("Can't find stream function.");
			return;
		}

		String streamFunction[] = m.group().split("F");
		int streamNo = Integer.parseInt(streamFunction[0].replace("S", "").trim());
		int functionNo = Integer.parseInt(streamFunction[1].trim());

		needReply = functionNo % 2 == 0 ? false : true;

		Queue<String> originalQueue = SECSMsgUtil.getInstance().parseStrMsgToQueue(originalSecsStr);

		if (originalQueue == null) {
			System.out.println("Parse SECS message fail.");
		} else {
			SECSMsg secsMsg = new SECSMsg();
			// header
			SECSHeader header = new SECSHeader();
			header.setDeviceId(Integer.parseInt(configBean.getInnerConfig().getDeviceId()));
			header.setFunctionNo(functionNo);
			header.setNeedReply(needReply);
			header.setpType(null);
			header.setSessionId(0);
			header.setStreamNo(streamNo);
			header.setsType(SessionType.DataMessage);
			header.setSystemByte("FFFFFFFF");
			secsMsg.setHeader(header);

			// body
			if (originalQueue.size() != 0) {
				SECSBody body = new SECSBody();
				Queue<String> copyQueue = new LinkedBlockingQueue<String>(originalQueue);
				body.setOriginQueue(copyQueue);
				LinkSecsItem secsBody = SECSMsgUtil.getInstance().buildSecs(originalQueue);
				body.setRootItem(secsBody);
				secsMsg.setBody(body);
			}

			msgBridgeImpl.sendMsg(secsMsg);

		}}catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getLocalizedMessage());
			
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
					// secslocation(event);
				}
			}

		});
		
		Main.getStage().setOnCloseRequest(event -> {
		    // 窗口关闭事件处理代码
			System.exit(0);
		});

		configBean.getInnerConfig().addPropertyChangeListener(lister);

		
		PrintStream ps = new PrintStream(new OutputStreamByTextArea(txtAreaConsole));
		System.setOut(ps);
		System.setErr(ps);
		
		
		
		preCount = 0;
		mainStringBuffer = new StringBuffer();
	
		
		/*
		Task task =new Task() {

			@Override
			protected Object call() throws Exception {
				// TODO Auto-generated method stub
				
				return null;
			}

			
			
			@Override
			protected void running() {
				// TODO Auto-generated method stub
	
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
					
				
				txtAreaReciver.setText(mainStringBuffer.toString());
				txtAreaReciver.setScrollTop(Double.MAX_VALUE);
			}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		task.run();
		
		
		
		
			ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
			int initialDelay = 0; // 初始延迟时间为0秒
			int period = 100; // 间隔时间为50 ms

			scheduler.scheduleAtFixedRate(task, initialDelay, period,  TimeUnit.MILLISECONDS);
	*/
			
			
			
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		int initialDelay = 0; 
		int period = 100;  
		scheduler.scheduleAtFixedRate(()->{
			//get string from stringbuffer.
			//judge length
			//preCount = mainStringBuffer.length()!=preCount?mainStringBuffer.length():preCount;
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
				txtAreaReciver.setText(mainStringBuffer.toString());
				txtAreaReciver.setScrollTop(Double.MAX_VALUE);

				});
			}}, initialDelay, period, TimeUnit.MILLISECONDS); 
		
	
		
		/*
		txtAreaConsole.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            	try {
                if(newValue.split("\n").length>1000){
                    int start = newValue.indexOf("\n")+1;
                    txtAreaConsole.setText(newValue.substring(start));
                    txtAreaConsole.setScrollTop(Double.MAX_VALUE);  // 让滚动条保留在最后面
                }
            	}catch(Exception e) {
                logger.error(e.getMessage());	
                throw e;
                }
            }
        });*/ 
		/*
		txtAreaReciver.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            	try {
            	String localTmp = newValue;
            		Platform.runLater(() -> {
            		
                if(localTmp.split("\n").length>1000){
                    int start = localTmp.indexOf("\n")+1;
                    txtAreaReciver.textProperty().set(localTmp.substring(start));
                    txtAreaReciver.setScrollTop(Double.MAX_VALUE);  // 让滚动条保留在最后面
                }
                });
            	}catch(Exception e) {
                logger.error(e.getMessage());	
                throw e;
                }finally {
                	oldValue=null;
                	newValue=null;
                	observable=null;
                }
            }
        }); 
		*/
		
		/*
		UnaryOperator<Change> unaryOperator = new RowConstrain();
		TextFormatter<String> tmpTextFormatter = new TextFormatter<String>(unaryOperator);
		txtAreaReciver.setTextFormatter(tmpTextFormatter);
		*/
		
		/*
		  Pattern newline = Pattern.compile("\n");
		  
		  txtAreaReciver.setTextFormatter(new TextFormatter<String>(change ->  {
			  
	            String newText = change.getControlNewText();

	            // count lines in proposed new text:
	            Matcher matcher = newline.matcher(newText);
	            int lines = 1 ;
	            
	            //while (matcher.find()) lines++;

	            lines = newText.split("\n").length;
	            
	            // if there aren't too many lines just return the changed unmodified:
	            if (lines <= 100) return change ;

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

	            
	            return null  ;
			 
	        }));*/
		
	
	}

	
	public void changeIpStr() {
		// TODO Auto-generated method stub
		Platform.runLater(() -> {
			configBean.getInnerConfig();
			String tmp =configBean.getInnerConfig().getConnectionMode()+":"+ configBean.getInnerConfig().getIpLocal() + ":" + configBean.getInnerConfig().getPortLocal()
					+ " <-> " + configBean.getInnerConfig().getIpRemote() + ":"
					+ configBean.getInnerConfig().getPortRemote();

			// lblConnectionInfo.setBackground(new Background(new
			// BackgroundFill(Color.GREEN,null,null)));
			lblConnectionInfo.setText(tmp);

		});

	}

}
