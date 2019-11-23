package com.agri.monitor.vo;

public class ProTeamAndEmployInfoQueryVO extends CommonQueryVO{
	private Integer date_year;
	private Integer date_year1;
	private String towns;

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

	public Integer getDate_year1() {
		return date_year1;
	}

	public void setDate_year1(Integer date_year1) {
		this.date_year1 = date_year1;
	}

	@Override
	public String toString() {
		return "AgriBaseinfoQueryVO [date_year=" + date_year + ", date_year1=" + date_year1 + ", towns=" + towns + "]";
	}
}
