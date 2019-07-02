package com.agri.monitor.vo;

public class CommonQueryVO {
	private int page;
	private int limit;
	private int currIndex;
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		if(limit>0) {
			this.currIndex = (page - 1) * limit;
		}
		this.page = page;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		if(page>0) {
			this.currIndex = (page - 1) * limit;
		}
		this.limit = limit;
	}
	public int getCurrIndex() {
		return currIndex;
	}
	public void setCurrIndex(int currIndex) {
		this.currIndex = currIndex;
	}
}
