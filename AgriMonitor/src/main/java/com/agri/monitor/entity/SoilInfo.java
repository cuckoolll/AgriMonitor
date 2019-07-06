package com.agri.monitor.entity;

public class SoilInfo { 
	private String gid; 
	private int date_year; 
	private String county;
	private String towns;
	private String village;
	private String code_number;
	private double organic;
	private double nitrogen;
	private double phosphorus; 
	private double effective_phosphorus;
	private double potassium; 
	private double ph; 
	private double salinity;
	private double available_potassium;
	private double slow_release_potassium;
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
	public int getDate_year() {
		return date_year;
	}
	public void setDate_year(int date_year) {
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
	public String getCode_number() {
		return code_number;
	}
	public void setCode_number(String code_number) {
		this.code_number = code_number;
	}
	public double getOrganic() {
		return organic;
	}
	public void setOrganic(double organic) {
		this.organic = organic;
	}
	public double getNitrogen() {
		return nitrogen;
	}
	public void setNitrogen(double nitrogen) {
		this.nitrogen = nitrogen;
	}
	public double getPhosphorus() {
		return phosphorus;
	}
	public void setPhosphorus(double phosphorus) {
		this.phosphorus = phosphorus;
	}
	public double getEffective_phosphorus() {
		return effective_phosphorus;
	}
	public void setEffective_phosphorus(double effective_phosphorus) {
		this.effective_phosphorus = effective_phosphorus;
	}
	public double getPotassium() {
		return potassium;
	}
	public void setPotassium(double potassium) {
		this.potassium = potassium;
	}
	public double getPh() {
		return ph;
	}
	public void setPh(double ph) {
		this.ph = ph;
	}
	public double getSalinity() {
		return salinity;
	}
	public void setSalinity(double salinity) {
		this.salinity = salinity;
	}
	public double getAvailable_potassium() {
		return available_potassium;
	}
	public void setAvailable_potassium(double available_potassium) {
		this.available_potassium = available_potassium;
	}
	public double getSlow_release_potassium() {
		return slow_release_potassium;
	}
	public void setSlow_release_potassium(double slow_release_potassium) {
		this.slow_release_potassium = slow_release_potassium;
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
