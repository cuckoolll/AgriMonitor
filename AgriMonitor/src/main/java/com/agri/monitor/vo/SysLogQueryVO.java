package com.agri.monitor.vo;

public class SysLogQueryVO extends CommonQueryVO{
	private Integer operation_type;
	private Integer operation_status;
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
	@Override
	public String toString() {
		return "SysLogQueryVO [operation_type=" + operation_type + ", operation_status=" + operation_status + "]";
	}
}
