package com.agri.monitor.entity;

import java.util.Date;

public class AgriBaseinfo {
	private Integer gid;
	private String county;
	private String towns;
	private String village;
	private Integer date_year;
	private Integer rksl;
	private Double gtmj;
	private Double gdmj;
	private Double gbzntmj;
	private Double ccmj;
	private String nzwzl;
	private Double nzzmj;
	private String zzfs;
	
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
	public Integer getRksl() {
		return rksl;
	}
	public void setRksl(Integer rksl) {
		this.rksl = rksl;
	}
	public Double getGtmj() {
		return gtmj;
	}
	public void setGtmj(Double gtmj) {
		this.gtmj = gtmj;
	}
	public Double getGdmj() {
		return gdmj;
	}
	public void setGdmj(Double gdmj) {
		this.gdmj = gdmj;
	}
	public Double getGbzntmj() {
		return gbzntmj;
	}
	public void setGbzntmj(Double gbzntmj) {
		this.gbzntmj = gbzntmj;
	}
	public Double getCcmj() {
		return ccmj;
	}
	public void setCcmj(Double ccmj) {
		this.ccmj = ccmj;
	}
	public String getNzwzl() {
		return nzwzl;
	}
	public void setNzwzl(String nzwzl) {
		this.nzwzl = nzwzl;
	}
	public Double getNzzmj() {
		return nzzmj;
	}
	public void setNzzmj(Double nzzmj) {
		this.nzzmj = nzzmj;
	}
	public String getZzfs() {
		return zzfs;
	}
	public void setZzfs(String zzfs) {
		this.zzfs = zzfs;
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
	public Integer getDate_year() {
		return date_year;
	}
	public void setDate_year(Integer date_year) {
		this.date_year = date_year;
	}
}
