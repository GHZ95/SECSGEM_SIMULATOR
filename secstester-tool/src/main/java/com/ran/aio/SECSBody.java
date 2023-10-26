package com.ran.aio;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class SECSBody {

		private SECSItem root;
		private int bodySize;
		private LinkSecsItem rootItem;
		private Queue<String> originQueue;
		
		
		
		public SECSBody() {
			// TODO Auto-generated constructor stub
			root = new SECSItem();
			bodySize= 0;
		}
		
		
		public boolean append(SECSItem item) {
			boolean rtnFlag = true;
			
			if(item.getFormat().equals(SECSFormat.L)) {
				List<SECSItem> childList = new ArrayList<SECSItem>();
			}
			
			
			return rtnFlag;
			
		}


		
		
		public Queue<String> getOriginQueue() {
			return originQueue;
		}


		public void setOriginQueue(Queue<String> originQueue) {
			this.originQueue = originQueue;
		}


		public SECSItem getRoot() {
			return root;
		}


		public void setRoot(SECSItem root) {
			this.root = root;
		}


		public int getBodySize() {
			return bodySize;
		}


		public void setBodySize(int bodySize) {
			this.bodySize = bodySize;
		}


		public LinkSecsItem getRootItem() {
			return rootItem;
		}


		public void setRootItem(LinkSecsItem rootItem) {
			this.rootItem = rootItem;
		}


		@Override
		public String toString() {
			
			return rootItem.toString();
		}
		
		
}
