package com.agri.monitor.vo;

import java.util.Date;

public class AirQueryVO extends CommonQueryVO {
	private String city;
	private String quality_time;
	private String station_name;
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getQuality_time() {
		return quality_time;
	}
	public void setQuality_time(String quality_time) {
		this.quality_time = quality_time;
	}
	public String getStation_name() {
		return station_name;
	}
	public void setStation_name(String station_name) {
		this.station_name = station_name;
	} 

	
}
