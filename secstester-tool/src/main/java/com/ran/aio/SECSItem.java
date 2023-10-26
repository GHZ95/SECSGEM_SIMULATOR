package com.ran.aio;

import java.util.List;

public class SECSItem {

	private int layer;
	private int order;
	private String describe;
	private String value;
	private SECSFormat format;
	private String attr;
	private SECSItem previousItem;
	private SECSItem[] childItem;
	
	public SECSItem() {
		// TODO Auto-generated constructor stub
		layer = 0; 
		order =0;
	}
	
	public int getLayer() {
		return layer;
	}
	public void setLayer(int layer) {
		this.layer = layer;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public SECSFormat getFormat() {
		return format;
	}
	public void setFormat(SECSFormat format) {
		this.format = format;
	}
	public String getAttr() {
		return attr;
	}
	public void setAttr(String attr) {
		this.attr = attr;
	}
	public SECSItem getPreviousItem() {
		return previousItem;
	}
	public void setPreviousItem(SECSItem previousItem) {
		this.previousItem = previousItem;
	}

	public SECSItem[] getChildItem() {
		return childItem;
	}

	public void setChildItem(SECSItem[] childItem) {
		this.childItem = childItem;
	}
	
	
	
	
}
