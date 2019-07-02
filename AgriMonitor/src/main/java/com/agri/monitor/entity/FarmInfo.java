package com.agri.monitor.entity;

import java.util.Date;

public class FarmInfo {
	private Integer gid;
	private String county;
	private String towns;
	private String village;
	private String farm_name;
	private String farm_address;
	private String legal_person;
	private String phone_num;
	private Integer animals_type;
	private Integer animals_size;
	private String remarks;
	private Date create_time;
	private Integer creator;
	private Integer modifier;
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
	public String getFarm_name() {
		return farm_name;
	}
	public void setFarm_name(String farm_name) {
		this.farm_name = farm_name;
	}
	public String getFarm_address() {
		return farm_address;
	}
	public void setFarm_address(String farm_address) {
		this.farm_address = farm_address;
	}
	public String getLegal_person() {
		return legal_person;
	}
	public void setLegal_person(String legal_person) {
		this.legal_person = legal_person;
	}
	public String getPhone_num() {
		return phone_num;
	}
	public void setPhone_num(String phone_num) {
		this.phone_num = phone_num;
	}
	public Integer getAnimals_type() {
		return animals_type;
	}
	public void setAnimals_type(Integer animals_type) {
		this.animals_type = animals_type;
	}
	public Integer getAnimals_size() {
		return animals_size;
	}
	public void setAnimals_size(Integer animals_size) {
		this.animals_size = animals_size;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Integer getCreator() {
		return creator;
	}
	public void setCreator(Integer creator) {
		this.creator = creator;
	}
	public Integer getModifier() {
		return modifier;
	}
	public void setModifier(Integer modifier) {
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
		return "FarmInfo [gid=" + gid + ", county=" + county + ", towns=" + towns + ", village=" + village
				+ ", farm_name=" + farm_name + ", farm_address=" + farm_address + ", legal_person=" + legal_person
				+ ", phone_num=" + phone_num + ", animals_type=" + animals_type + ", animals_size=" + animals_size
				+ ", remarks=" + remarks + ", create_time=" + create_time + ", creator=" + creator + ", modifier="
				+ modifier + ", last_time=" + last_time + "]";
	}
	
}
