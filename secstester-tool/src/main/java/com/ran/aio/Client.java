package com.ran.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;

public class Client {

	
	 public void clientBoot(int port) throws IOException {

	        // 创建 Client
	        AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();
	        // 与 Server 连接
	        try {
				socketChannel.connect(new InetSocketAddress("127.0.0.1", 8000)).get();
				
				
				// 向 Server 写数据
		        socketChannel.write(ByteBuffer.wrap("HelloServer".getBytes()));
		        // 读取 Server 的数据
		        ByteBuffer buffer = ByteBuffer.allocate(512);
		        Integer len = socketChannel.read(buffer).get();
		        if (len != -1) {
		            System.out.println("客户端收到消息：" + new String(buffer.array(), 0, len));
		        }
				
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
 
		 
		 
	 }
}
