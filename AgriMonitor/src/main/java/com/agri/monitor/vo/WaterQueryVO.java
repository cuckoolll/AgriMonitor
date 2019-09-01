package com.agri.monitor.vo;

public class WaterQueryVO extends CommonQueryVO {
	private String county;
	private String quality_time;
	private String quality_time1;
	private String quality_type;
	private String quality_address;

	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getQuality_time() {
		return quality_time;
	}
	public void setQuality_time(String quality_time) {
		this.quality_time = quality_time;
	}
	public String getQuality_time1() {
		return quality_time1;
	}
	public void setQuality_time1(String quality_time1) {
		this.quality_time1 = quality_time1;
	}
	public String getQuality_type() {
		return quality_type;
	}
	public void setQuality_type(String quality_type) {
		this.quality_type = quality_type;
	}	
	public String getQuality_address() {
		return quality_address;
	}
	public void setQuality_address(String quality_address) {
		this.quality_address = quality_address;
	}
}
