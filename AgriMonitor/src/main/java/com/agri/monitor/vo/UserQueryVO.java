package com.agri.monitor.vo;

public class UserQueryVO extends CommonQueryVO{
	private Integer user_role;
	
	public Integer getUser_role() {
		return user_role;
	}
	public void setUser_role(Integer user_role) {
		this.user_role = user_role;
	}
	@Override
	public String toString() {
		return "UserQueryVO [user_role=" + user_role + "]";
	}
	
}
