package com.ran.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Server {

	AsynchronousServerSocketChannel serverSocket;
	private int localPort = 5000;
	Attachment att;
	
	@Autowired
	ChannelHandler channelHandler;
	
	private Server(){
		serverSocket = null;
		
	}//构造器私有化，防止new，导致多个实例
    private static volatile Server singleton;
    public static Server getInstance(){//向外暴露一个静态的公共方法  getInstance
        //第一层检查
        if(singleton == null){
            //同步代码块
            synchronized (Server.class){
                 //第二层检查
                if(singleton == null) {
                    singleton = new Server();
                }
            }

        }
        return singleton;
    }

    public void serverStop() {
    	
    	if(serverSocket!=null || serverSocket.isOpen()) {
    		try {
    			channelHandler.close();
				serverSocket.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
    	}
    }

    
    public boolean serverIsOpen() {
    	
    	return serverSocket==null?false:serverSocket.isOpen();
    }
    
	
	 public void serverBoot(int port) throws IOException {

		 setLocalPort(port);
         // 实例化，并监听端口
		 serverSocket =
               AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(port));

       // 自己定义一个 Attachment 类，用于传递一些信息
       att = new Attachment();
       
       //channelHandler = new ChannelHandler();
       att.setServer(serverSocket);

      
       serverSocket.accept(att, new CompletionHandler<AsynchronousSocketChannel, Attachment>() {
           @Override
           public void completed(AsynchronousSocketChannel client, Attachment att) {
               try {
                   SocketAddress clientAddr = client.getRemoteAddress();
                   System.out.println("Accept new connection:" + clientAddr);

                   // 收到新的连接后，server 应该重新调用 accept 方法等待新的连接进来
                   //att.getServer().accept(att, this);

                   Attachment newAtt = new Attachment();
                   newAtt.setServer(serverSocket);
                   newAtt.setClient(client);
                   newAtt.setReadMode(true);
                   newAtt.setBuffer(ByteBuffer.allocate(4));

                   // 这里也可以继续使用匿名实现类，不过代码不好看，所以这里专门定义一个类
                   client.read(newAtt.getBuffer(), newAtt,channelHandler);
               } catch (IOException ex) {
                   ex.printStackTrace();
               }
           }

           @Override
           public void failed(Throwable t, Attachment att) {
               System.out.println("accept failed");
           }
       });
       /*
       // 为了防止 main 线程退出
       try {
           Thread.currentThread().join();
       } catch (InterruptedException e) {
       }
   */
   }

	public int getLocalPort() {
		return localPort;
	}

	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

	public Attachment getAtt() {
		return att;
	}

	public void setAtt(Attachment att) {
		this.att = att;
	}

	public ChannelHandler getChannelHandler() {
		return channelHandler;
	}

	public void setChannelHandler(ChannelHandler channelHandler) {
		this.channelHandler = channelHandler;
	}
	 
	 
   
}