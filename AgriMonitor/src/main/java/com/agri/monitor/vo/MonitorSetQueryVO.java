package com.agri.monitor.vo;

public class MonitorSetQueryVO extends CommonQueryVO{
	private Integer monitor_type;
	private Integer stopflag;

	public Integer getMonitor_type() {
		return monitor_type;
	}

	public void setMonitor_type(Integer monitor_type) {
		this.monitor_type = monitor_type;
	}

	public Integer getStopflag() {
		return stopflag;
	}

	public void setStopflag(Integer stopflag) {
		this.stopflag = stopflag;
	}

	@Override
	public String toString() {
		return "MonitorSetQueryVO [monitor_type=" + monitor_type + ", stopflag=" + stopflag + "]";
	}
	
	
}
