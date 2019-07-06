package com.agri.monitor.entity;

import java.util.Date;

public class AnimalsType {
	private Integer gid;
	private String type_name;
	private Date create_time;
	private String creator;
	private String modifier;
	private Date last_time;
	private Integer stopflag;
	
	public Integer getGid() {
		return gid;
	}
	public void setGid(Integer gid) {
		this.gid = gid;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
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
	public Integer getStopflag() {
		return stopflag;
	}
	public void setStopflag(Integer stopflag) {
		this.stopflag = stopflag;
	}
	@Override
	public String toString() {
		return "AnimalsType [gid=" + gid + ", type_name=" + type_name + ", creator=" + creator + ", modifier="
				+ modifier + ", stopflag=" + stopflag + "]";
	}
	
}
