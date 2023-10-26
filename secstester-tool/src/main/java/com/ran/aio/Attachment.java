package com.ran.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;

public class Attachment {
	 private AsynchronousServerSocketChannel server;
	    private AsynchronousSocketChannel client;
	    private boolean isReadMode;
	    private ByteBuffer buffer;
	    
	    
	    
		public Attachment() {
			super();
		}
		public Attachment(AsynchronousServerSocketChannel server, AsynchronousSocketChannel client, boolean isReadMode,
				ByteBuffer buffer) {
			super();
			this.server = server;
			this.client = client;
			this.isReadMode = isReadMode;
			this.buffer = buffer;
		}
		public AsynchronousServerSocketChannel getServer() {
			return server;
		}
		public void setServer(AsynchronousServerSocketChannel server) {
			this.server = server;
		}
		public AsynchronousSocketChannel getClient() {
			return client;
		}
		public void setClient(AsynchronousSocketChannel client) {
			this.client = client;
		}
		public boolean isReadMode() {
			return isReadMode;
		}
		public void setReadMode(boolean isReadMode) {
			this.isReadMode = isReadMode;
		}
		public ByteBuffer getBuffer() {
			return buffer;
		}
		public void setBuffer(ByteBuffer buffer) {
			this.buffer = buffer;
		}
	    
	    
}
