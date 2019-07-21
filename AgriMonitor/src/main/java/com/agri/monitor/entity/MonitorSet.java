package com.agri.monitor.entity;

import java.util.Date;

public class MonitorSet {
	private Integer gid;
	private Integer monitor_type;
	private Integer target_type;
	private String target;
	private String conditions;
	private Double value_set;
	private Date create_time;
	private String creator;
	private String modifier;
	private Date last_time;
	private Integer stopflag;
	private String condition_showname;
	public Integer getGid() {
		return gid;
	}
	public void setGid(Integer gid) {
		this.gid = gid;
	}
	public Integer getMonitor_type() {
		return monitor_type;
	}
	public void setMonitor_type(Integer monitor_type) {
		this.monitor_type = monitor_type;
	}
	public Integer getTarget_type() {
		return target_type;
	}
	public void setTarget_type(Integer target_type) {
		this.target_type = target_type;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getConditions() {
		return conditions;
	}
	public void setConditions(String conditions) {
		this.conditions = conditions;
	}
	public Double getValue_set() {
		return value_set;
	}
	public void setValue_set(Double value_set) {
		this.value_set = value_set;
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
	public String getCondition_showname() {
		return condition_showname;
	}
	public void setCondition_showname(String condition_showname) {
		this.condition_showname = condition_showname;
	}
	@Override
	public String toString() {
		return "MonitorSet [gid=" + gid + ", monitor_type=" + monitor_type + ", target_type=" + target_type
				+ ", target=" + target + ", conditions=" + conditions + ", value_set=" + value_set + ", creator="
				+ creator + ", modifier=" + modifier + ", stopflag=" + stopflag + ", condition_showname="
				+ condition_showname + "]";
	}
	
}
