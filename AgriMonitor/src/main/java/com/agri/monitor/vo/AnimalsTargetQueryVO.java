package com.agri.monitor.vo;

public class AnimalsTargetQueryVO extends CommonQueryVO{
	private Integer stopflag;

	public Integer getStopflag() {
		return stopflag;
	}

	public void setStopflag(Integer stopflag) {
		this.stopflag = stopflag;
	}

	@Override
	public String toString() {
		return "AnimalsTargetQueryVO [stopflag=" + stopflag + "]";
	}
	
}
