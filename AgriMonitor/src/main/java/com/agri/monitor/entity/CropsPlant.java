package com.agri.monitor.entity;

import java.util.Date;

public class CropsPlant {
	private Integer gid;
	private String county;
	private String towns;
	private String village;
	private Integer date_year;
	private Integer crops_type;
	private Double planted_area;
	private Double planted_output;
	private Date create_time;
	private String creator;
	private String modifier;
	private Date last_time;
	public Integer getGid() {
		return gid;
	}
	public void setGid(Integer gid) {
		this.gid = gid;
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
	public Integer getDate_year() {
		return date_year;
	}
	public void setDate_year(Integer date_year) {
		this.date_year = date_year;
	}
	public Integer getCrops_type() {
		return crops_type;
	}
	public void setCrops_type(Integer crops_type) {
		this.crops_type = crops_type;
	}
	public Double getPlanted_area() {
		return planted_area;
	}
	public void setPlanted_area(Double planted_area) {
		this.planted_area = planted_area;
	}
	public Double getPlanted_output() {
		return planted_output;
	}
	public void setPlanted_output(Double planted_output) {
		this.planted_output = planted_output;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public Date getLast_time() {
		return last_time;
	}
	public void setLast_time(Date last_time) {
		this.last_time = last_time;
	}
	@Override
	public String toString() {
		return "CropsPlant [gid=" + gid + ", county=" + county + ", towns=" + towns + ", village=" + village
				+ ", date_year=" + date_year + ", crops_type=" + crops_type + ", planted_area=" + planted_area
				+ ", planted_output=" + planted_output + "]";
	}
}
