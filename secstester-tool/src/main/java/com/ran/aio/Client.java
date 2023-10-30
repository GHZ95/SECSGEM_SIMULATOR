package com.ran.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Client {

	
	@Autowired
	ChannelHandler channelHandler;
	
	AsynchronousSocketChannel socketChannel;
	
	 public void clientBoot(String ip,int port) throws IOException {

	        // 创建 Client
	        socketChannel = AsynchronousSocketChannel.open();
	        // 与 Server 连接
	      
	        	Attachment att = new Attachment();
	        	att.setServer(null);
	        	att.setClient(socketChannel);
	        	att.setReadMode(true);
	        	att.setBuffer(ByteBuffer.allocate(4));
	        	
	        	//socketChannel.connect(new InetSocketAddress(ip, port)).get();
	        	InetSocketAddress itAddress = new InetSocketAddress(ip, port);
	        	socketChannel.connect(itAddress, att, new CompletionHandler<Void,Attachment>(){


					@Override
					public void failed(Throwable exc, Attachment attachment) {
						// TODO Auto-generated method stub
						System.out.println("active failed");
					}

					@Override
					public void completed(Void result, Attachment attachment) {
						// TODO Auto-generated method stub
						
						try {
							
							
							
							
							socketChannel.read(att.getBuffer(), att,channelHandler);
							//send select req
							channelHandler.writeChannelForRqstSelect(socketChannel);
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}

		
	        		
	        	});
			

 
		 
		 
	 }
	 
	 public boolean clientIsOpen() {
		 return socketChannel.isOpen();
	 }
	 
	 public boolean clientClose() {
		 try {
			 if(socketChannel!=null && socketChannel.isOpen())
			socketChannel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		 return true;
	 }
}
