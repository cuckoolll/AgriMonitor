package com.agri.monitor.vo;

public class FarmQueryVO extends CommonQueryVO{
	private String farm_name;
	private Integer animals_type;
	
	public String getFarm_name() {
		return farm_name;
	}
	public void setFarm_name(String farm_name) {
		this.farm_name = farm_name;
	}
	public Integer getAnimals_type() {
		return animals_type;
	}
	public void setAnimals_type(Integer animals_type) {
		this.animals_type = animals_type;
	}
	@Override
	public String toString() {
		return "FarmQueryVO [farm_name=" + farm_name + ", animals_type=" + animals_type + "]";
	}
	
}
