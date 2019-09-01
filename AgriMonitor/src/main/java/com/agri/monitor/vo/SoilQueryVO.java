package com.agri.monitor.vo;

public class SoilQueryVO extends CommonQueryVO {
	private String county;
	private String towns;
	private String date_year;
	private String date_year1;
	private String code_number;

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
	public String getDate_year1() {
		return date_year1;
	}
	public void setDate_year1(String date_year1) {
		this.date_year1 = date_year1;
	}
	public String getTowns() {
		return towns;
	}
	public void setTowns(String towns) {
		this.towns = towns;
	}
	public String getCode_number() {
		return code_number;
	}
	public void setCode_number(String code_number) {
		this.code_number = code_number;
	}
	
}
