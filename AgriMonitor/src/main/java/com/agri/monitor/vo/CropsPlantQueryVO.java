package com.agri.monitor.vo;

public class CropsPlantQueryVO extends CommonQueryVO{
	private String towns;
	private Integer date_year;
	private Integer crops_type;
	public String getTowns() {
		return towns;
	}
	public void setTowns(String towns) {
		this.towns = towns;
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
	@Override
	public String toString() {
		return "CropsPlantQueryVO [towns=" + towns + ", date_year=" + date_year + ", crops_type=" + crops_type + "]";
	}
	
}
