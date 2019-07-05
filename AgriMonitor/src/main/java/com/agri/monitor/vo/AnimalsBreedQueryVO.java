package com.agri.monitor.vo;

public class AnimalsBreedQueryVO extends CommonQueryVO{
	private Integer date_month;

	public Integer getDate_month() {
		return date_month;
	}

	public void setDate_month(Integer date_month) {
		this.date_month = date_month;
	}

	@Override
	public String toString() {
		return "AnimalsBreedQueryVO [date_month=" + date_month + "]";
	}
}
