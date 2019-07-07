package com.agri.monitor.entity;

import java.util.Date;

public class AnimalsTarget {
	private Integer gid;
	private String target_name;
	private Integer parent_id;
	private Date create_time;
	private String creator;
	private String modifier;
	private Date last_time;
	private Integer stopflag;
	private String parent_name;
	private Integer isleaf;
	
	public Integer getGid() {
		return gid;
	}
	public void setGid(Integer gid) {
		this.gid = gid;
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
	public String getTarget_name() {
		return target_name;
	}
	public void setTarget_name(String target_name) {
		this.target_name = target_name;
	}
	public Integer getParent_id() {
		return parent_id;
	}
	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
	}
	
	public String getParent_name() {
		return parent_name;
	}
	public void setParent_name(String parent_name) {
		this.parent_name = parent_name;
	}
	public Integer getIsleaf() {
		return isleaf;
	}
	public void setIsleaf(Integer isleaf) {
		this.isleaf = isleaf;
	}
	@Override
	public String toString() {
		return "AnimalsTarget [gid=" + gid + ", target_name=" + target_name + ", parent_id=" + parent_id + ", stopflag="
				+ stopflag + "]";
	}
}
