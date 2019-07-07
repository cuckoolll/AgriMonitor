package com.agri.monitor.vo;

public class AnimalsBreedQueryVO extends CommonQueryVO{
	private Integer date_month;
	private String towns;
	private Integer animalsTarget;

	public Integer getDate_month() {
		return date_month;
	}

	public void setDate_month(Integer date_month) {
		this.date_month = date_month;
	}

	public String getTowns() {
		return towns;
	}

	public void setTowns(String towns) {
		this.towns = towns;
	}

	public Integer getAnimalsTarget() {
		return animalsTarget;
	}

	public void setAnimalsTarget(Integer animalsTarget) {
		this.animalsTarget = animalsTarget;
	}

	@Override
	public String toString() {
		return "AnimalsBreedQueryVO [date_month=" + date_month + ", towns=" + towns + ", animalsTarget=" + animalsTarget
				+ "]";
	}

}
