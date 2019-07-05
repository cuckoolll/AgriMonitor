package com.agri.monitor.entity;

public class SoilInfo { 
	private String gid; 
	private String date_year; 
	private String county;
	private String towns;
	private String village;
	private double year_avg_temperature;
	private double high_temperature;
	private double low_temperature;
	private double year_precipitation; 
	private double mouth_high_precipitation;
	private double day_high_precipitation; 
	private double year_avg_winds; 
	private double high_winds;
	private int year_high_winds_days; 
	private double year_avg_pressure;
	private int year_thunderstorm_days; 
	private int year_sandstorm_days;
	private String create_time; 
	private String creator; 
	private String last_time; 
	private String modifier;
	
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public String getDate_year() {
		return date_year;
	}
	public void setDate_year(String date_year) {
		this.date_year = date_year;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getTowns() {
		return towns;
	}
	public void setTowns(String towns) {
		this.towns = towns;
	}
	public String getVillage() {
		return village;
	}
	public void setVillage(String village) {
		this.village = village;
	}
	public double getYear_avg_temperature() {
		return year_avg_temperature;
	}
	public void setYear_avg_temperature(double year_avg_temperature) {
		this.year_avg_temperature = year_avg_temperature;
	}
	public double getHigh_temperature() {
		return high_temperature;
	}
	public void setHigh_temperature(double high_temperature) {
		this.high_temperature = high_temperature;
	}
	public double getLow_temperature() {
		return low_temperature;
	}
	public void setLow_temperature(double low_temperature) {
		this.low_temperature = low_temperature;
	}
	public double getYear_precipitation() {
		return year_precipitation;
	}
	public void setYear_precipitation(double year_precipitation) {
		this.year_precipitation = year_precipitation;
	}
	public double getMouth_high_precipitation() {
		return mouth_high_precipitation;
	}
	public void setMouth_high_precipitation(double mouth_high_precipitation) {
		this.mouth_high_precipitation = mouth_high_precipitation;
	}
	public double getDay_high_precipitation() {
		return day_high_precipitation;
	}
	public void setDay_high_precipitation(double day_high_precipitation) {
		this.day_high_precipitation = day_high_precipitation;
	}
	public double getYear_avg_winds() {
		return year_avg_winds;
	}
	public void setYear_avg_winds(double year_avg_winds) {
		this.year_avg_winds = year_avg_winds;
	}
	public double getHigh_winds() {
		return high_winds;
	}
	public void setHigh_winds(double high_winds) {
		this.high_winds = high_winds;
	}
	public int getYear_high_winds_days() {
		return year_high_winds_days;
	}
	public void setYear_high_winds_days(int year_high_winds_days) {
		this.year_high_winds_days = year_high_winds_days;
	}
	public double getYear_avg_pressure() {
		return year_avg_pressure;
	}
	public void setYear_avg_pressure(double year_avg_pressure) {
		this.year_avg_pressure = year_avg_pressure;
	}
	public int getYear_thunderstorm_days() {
		return year_thunderstorm_days;
	}
	public void setYear_thunderstorm_days(int year_thunderstorm_days) {
		this.year_thunderstorm_days = year_thunderstorm_days;
	}
	public int getYear_sandstorm_days() {
		return year_sandstorm_days;
	}
	public void setYear_sandstorm_days(int year_sandstorm_days) {
		this.year_sandstorm_days = year_sandstorm_days;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getLast_time() {
		return last_time;
	}
	public void setLast_time(String last_time) {
		this.last_time = last_time;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	} 
}
