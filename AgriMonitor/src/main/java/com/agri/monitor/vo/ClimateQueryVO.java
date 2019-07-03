package com.agri.monitor.vo;

public class ClimateQueryVO extends CommonQueryVO {
	private String county;
	private String date_year;

	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getDate_year() {
		return date_year;
	}
	public void setDate_year(String date_year) {
		this.date_year = date_year;
	}
}
