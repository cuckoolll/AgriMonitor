package com.agri.monitor.entity;

import java.util.Date;

public class MonitorLog {
	private Integer gid;
	
	private Integer setgid;
	
	private Date create_time;
	
	private String log;
	
	private Integer stopflag;

	private Double ratio;
	
	public Integer getGid() {
		return gid;
	}

	public void setGid(Integer gid) {
		this.gid = gid;
	}

	public Integer getSetgid() {
		return setgid;
	}

	public void setSetgid(Integer setgid) {
		this.setgid = setgid;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public Integer getStopflag() {
		return stopflag;
	}

	public void setStopflag(Integer stopflag) {
		this.stopflag = stopflag;
	}

	public Double getRatio() {
		return ratio;
	}

	public void setRatio(Double ratio) {
		this.ratio = ratio;
	}
	
}
