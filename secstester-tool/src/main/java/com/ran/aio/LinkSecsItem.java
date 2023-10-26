package com.ran.aio;

import java.util.List;

import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;

public class LinkSecsItem {

	private LinkSecsItem previous ;
	private LinkSecsItem next ;
	private String inner ;
	private List<LinkSecsItem> innerList ;
	
	
	private StringBuffer innerSb ;
	
	
	public LinkSecsItem() {
		// TODO Auto-generated constructor stub
	
	}

	public LinkSecsItem(List<LinkSecsItem> list) {
		// TODO Auto-generated constructor stub
		this.innerList = list;
	}
	
	public LinkSecsItem(String text) {
		// TODO Auto-generated constructor stub
		this.inner = text;
	}

	
	public LinkSecsItem getPrevious() {
		return previous;
	}

	public void setPrevious(LinkSecsItem previous) {
		this.previous = previous;
	}

	public LinkSecsItem getNext() {
		return next;
	}

	public void setNext(LinkSecsItem next) {
		this.next = next;
	}

	public String getInner() {
		return inner;
	}

	public void setInner(String inner) {
		this.inner = inner;
	}

	public List<LinkSecsItem> getInnerList() {
		return innerList;
	}

	public void setInnerList(List<LinkSecsItem> innerList) {
		this.innerList = innerList;
	}

	@Override
	public String toString() {

		innerSb = new StringBuffer();
		ite(this, 0);
		return innerSb.toString();
	}
	
	public void ite(LinkSecsItem item,int layer) {
		for(int p = layer ;p>0;p--) {
			//System.out.print("-");
			innerSb.append("-");
		
		}

		if(item.getInnerList()!=null) {
			layer++;
			
			//System.out.println("<L["+item.getInnerList().size()+"]");
			innerSb.append("<L["+item.getInnerList().size()+"]");
			innerSb.append(System.getProperty("line.separator"));
			for(int i = 0 ; i < item.getInnerList().size();i++) {
				
				ite(item.getInnerList().get(i),layer);
			}
			for(int p = layer ;p>0;p--) {
				//System.out.print("-");
				innerSb.append("-");
			}

			//System.out.println(">");
			innerSb.append(">");
			innerSb.append(System.getProperty("line.separator"));
		}else {
			//System.out.println("layer:"+layer+" value:"+item.getInner());
			//System.out.println(item.getInner());
			innerSb.append(item.getInner());
			innerSb.append(System.getProperty("line.separator"));
		}
		
	}
	
	
	
	
	
}
