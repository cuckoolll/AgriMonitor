package com.agri.monitor.entity;

import java.util.Date;

public class AnimalsBreed {
	private Integer gid;
	private Integer date_month;
	private String county;
	private String towns;
	private String village;
	private Integer animals_target;
	private Double surplus_size;
	private Double female_size;
	private Double child_size;
	private Double survival_size;
	private Double death_size;
	private Double maturity_size;
	private Double sell_size;
	private Double meat_output;
	private Double milk_output;
	private Double egg_output;
	private Double hair_output;
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
	public Integer getDate_month() {
		return date_month;
	}
	public void setDate_month(Integer date_month) {
		this.date_month = date_month;
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
	public Integer getAnimals_target() {
		return animals_target;
	}
	public void setAnimals_target(Integer animals_target) {
		this.animals_target = animals_target;
	}
	public Double getSurplus_size() {
		return surplus_size;
	}
	public void setSurplus_size(Double surplus_size) {
		this.surplus_size = surplus_size;
	}
	public Double getFemale_size() {
		return female_size;
	}
	public void setFemale_size(Double female_size) {
		this.female_size = female_size;
	}
	public Double getChild_size() {
		return child_size;
	}
	public void setChild_size(Double child_size) {
		this.child_size = child_size;
	}
	public Double getSurvival_size() {
		return survival_size;
	}
	public void setSurvival_size(Double survival_size) {
		this.survival_size = survival_size;
	}
	public Double getDeath_size() {
		return death_size;
	}
	public void setDeath_size(Double death_size) {
		this.death_size = death_size;
	}
	public Double getMaturity_size() {
		return maturity_size;
	}
	public void setMaturity_size(Double maturity_size) {
		this.maturity_size = maturity_size;
	}
	public Double getSell_size() {
		return sell_size;
	}
	public void setSell_size(Double sell_size) {
		this.sell_size = sell_size;
	}
	public Double getMeat_output() {
		return meat_output;
	}
	public void setMeat_output(Double meat_output) {
		this.meat_output = meat_output;
	}
	public Double getMilk_output() {
		return milk_output;
	}
	public void setMilk_output(Double milk_output) {
		this.milk_output = milk_output;
	}
	public Double getEgg_output() {
		return egg_output;
	}
	public void setEgg_output(Double egg_output) {
		this.egg_output = egg_output;
	}
	public Double getHair_output() {
		return hair_output;
	}
	public void setHair_output(Double hair_output) {
		this.hair_output = hair_output;
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
		return "AnimalsBreed [gid=" + gid + ", date_month=" + date_month + ", county=" + county + ", towns=" + towns
				+ ", village=" + village + ", animals_target=" + animals_target + ", surplus_size=" + surplus_size
				+ ", female_size=" + female_size + ", child_size=" + child_size + ", survival_size=" + survival_size
				+ ", death_size=" + death_size + ", maturity_size=" + maturity_size + ", sell_size=" + sell_size
				+ ", meat_output=" + meat_output + ", milk_output=" + milk_output + ", egg_output=" + egg_output
				+ ", hair_output=" + hair_output + ", create_time=" + create_time + ", creator=" + creator
				+ ", modifier=" + modifier + ", last_time=" + last_time + "]";
	}
}
