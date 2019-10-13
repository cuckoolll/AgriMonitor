package com.agri.monitor.vo;

public class GmhyzxxQueryVO extends CommonQueryVO{
	private Integer syear;
	private Integer eyear;
	
	public Integer getSyear() {
		return syear;
	}

	public void setSyear(Integer syear) {
		this.syear = syear;
	}

	public Integer getEyear() {
		return eyear;
	}

	public void setEyear(Integer eyear) {
		this.eyear = eyear;
	}

	@Override
	public String toString() {
		return "GmhyzxxQueryVO [syear=" + syear + ", eyear=" + eyear + "]";
	}

}
