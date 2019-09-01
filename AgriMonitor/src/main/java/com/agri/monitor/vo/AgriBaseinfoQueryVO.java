package com.agri.monitor.vo;

public class AgriBaseinfoQueryVO extends CommonQueryVO{
	private String towns;

	public String getTowns() {
		return towns;
	}

	public void setTowns(String towns) {
		this.towns = towns;
	}

	@Override
	public String toString() {
		return "AgriBaseinfoQueryVO [towns=" + towns + "]";
	}
	
}
