package com.agri.monitor.entity;

import java.util.Date;

public class UserInfo {
	private String user_id;
	
	private String user_showname;
	
	private String user_password;
	
	private Integer user_role;
	
	private Date create_time;
	
	private String creator;
	
	private String modifier;
	
	private Date last_time;
	
	private RoleInfo roleinfo;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_password() {
		return user_password;
	}

	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}

	public Integer getUser_role() {
		return user_role;
	}

	public void setUser_role(Integer user_role) {
		this.user_role = user_role;
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

	public RoleInfo getRoleinfo() {
		return roleinfo;
	}

	public void setRoleinfo(RoleInfo roleinfo) {
		this.roleinfo = roleinfo;
	}

	public String getUser_showname() {
		return user_showname;
	}

	public void setUser_showname(String user_showname) {
		this.user_showname = user_showname;
	}
}
