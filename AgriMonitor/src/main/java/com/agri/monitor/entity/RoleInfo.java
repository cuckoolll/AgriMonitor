package com.agri.monitor.entity;

import java.util.Date;

public class RoleInfo {
	private Integer gid;
	
	private String role_name;
	
	private String role_showname;
	
	private Date create_time;
	
	private Integer creator;
	
	private Integer modifier;
	
	private Date last_time;

	public Integer getGid() {
		return gid;
	}

	public void setGid(Integer gid) {
		this.gid = gid;
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	public String getRole_showname() {
		return role_showname;
	}

	public void setRole_showname(String role_showname) {
		this.role_showname = role_showname;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Integer getCreator() {
		return creator;
	}

	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	public Integer getModifier() {
		return modifier;
	}

	public void setModifier(Integer modifier) {
		this.modifier = modifier;
	}

	public Date getLast_time() {
		return last_time;
	}

	public void setLast_time(Date last_time) {
		this.last_time = last_time;
	}
	
}
