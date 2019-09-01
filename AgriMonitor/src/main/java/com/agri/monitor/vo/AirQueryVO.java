package com.agri.monitor.vo;

import java.util.Date;

import org.springframework.util.StringUtils;

public class AirQueryVO extends CommonQueryVO {
	private String city;
	private String quality_time;
	private String quality_time1;
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
		if (!StringUtils.isEmpty(quality_time)) {
			if (quality_time.split("-").length > 2) {
				this.quality_time = quality_time;
			} else {
				this.quality_time = quality_time + "-01";
			}
		} else {
			this.quality_time = quality_time;
		}
	}
	public String getQuality_time1() {
		return quality_time1;
	}
	public void setQuality_time1(String quality_time1) {
		if (!StringUtils.isEmpty(quality_time1)) {
			if (quality_time1.split("-").length > 2) {
				this.quality_time1 = quality_time1;
			} else {
				this.quality_time1 = quality_time1 + "-01";
			}
		} else {
			this.quality_time1 = quality_time1;
		}
	}
	public String getStation_name() {
		return station_name;
	}
	public void setStation_name(String station_name) {
		this.station_name = station_name;
	} 

	
}
