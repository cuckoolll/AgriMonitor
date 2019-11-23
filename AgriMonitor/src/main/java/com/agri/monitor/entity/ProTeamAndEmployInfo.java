package com.agri.monitor.entity;

import java.util.Date;

public class ProTeamAndEmployInfo {
	private Integer gid;
	private String county;
	private String towns;
	private String village;
	private Integer date_year;
	private Integer hs;
	private Integer rks_n;
	private Integer rks_v;
	private Integer nyrs;
	private Integer cmyrs;
	private Integer lyrs;
	private Integer fwyrs;
	private String bz;
	
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
	public Integer getHs() {
		return hs;
	}
	public void setHs(Integer hs) {
		this.hs = hs;
	}
	public Integer getRks_n() {
		return rks_n;
	}
	public void setRks_n(Integer rks_n) {
		this.rks_n = rks_n;
	}
	public Integer getRks_v() {
		return rks_v;
	}
	public void setRks_v(Integer rks_v) {
		this.rks_v = rks_v;
	}
	public Integer getNyrs() {
		return nyrs;
	}
	public void setNyrs(Integer nyrs) {
		this.nyrs = nyrs;
	}
	public Integer getCmyrs() {
		return cmyrs;
	}
	public void setCmyrs(Integer cmyrs) {
		this.cmyrs = cmyrs;
	}
	public Integer getLyrs() {
		return lyrs;
	}
	public void setLyrs(Integer lyrs) {
		this.lyrs = lyrs;
	}
	public Integer getFwyrs() {
		return fwyrs;
	}
	public void setFwyrs(Integer fwyrs) {
		this.fwyrs = fwyrs;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
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
}
