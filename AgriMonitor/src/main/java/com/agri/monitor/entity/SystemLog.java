package com.agri.monitor.entity;

import java.util.Date;

public class SystemLog {
	private Integer gid;
	private Integer operation_type;
	private Integer operation_status;
	private Integer operation_user;
	private String operation_log;
	private Date create_time;
	public Integer getGid() {
		return gid;
	}
	public void setGid(Integer gid) {
		this.gid = gid;
	}
	public Integer getOperation_type() {
		return operation_type;
	}
	public void setOperation_type(Integer operation_type) {
		this.operation_type = operation_type;
	}
	public Integer getOperation_status() {
		return operation_status;
	}
	public void setOperation_status(Integer operation_status) {
		this.operation_status = operation_status;
	}
	public Integer getOperation_user() {
		return operation_user;
	}
	public void setOperation_user(Integer operation_user) {
		this.operation_user = operation_user;
	}
	public String getOperation_log() {
		return operation_log;
	}
	public void setOperation_log(String operation_log) {
		this.operation_log = operation_log;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	
}
