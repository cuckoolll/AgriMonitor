package com.agri.monitor.enums;

public enum ClimateIndexEnum {
	
	YEAR_AVG_TEMPERATURE("year_avg_temperature", "年平均气温（摄氏度）"),
	HIGH_TEMPERATURE("high_temperature", "极端最高气温（摄氏度）"),
	LOW_TEMPERATURE("low_temperature", "极端最低气温（摄氏度）"),
	YEAR_PRECIPITATION("year_precipitation", "年降水量（毫米）"),
	MOUTH_HIGH_PRECIPITATION("mouth_high_precipitation", "月最大降水量（毫米）"),
	DAY_HIGH_PRECIPITATION("day_high_precipitation", "日最大降水量（毫米）"),
	YEAR_AVG_WINDS("year_avg_winds", "年平均最多风向（米每秒）"),
	HIGH_WINDS("high_winds", "极大风速（米每秒）"),
	YEAR_HIGH_WINDS_DAYS("year_high_winds_days", "年大风日数（天）"),
	YEAR_AVG_PRESSURE("year_avg_pressure", "年平均气压（百帕）"),
	YEAR_THUNDERSTORM_DAYS("year_thunderstorm_days", "年雷暴日数（次）"),
	year_sandstorm_days("year_sandstorm_days", "年尘暴日数（次）"),
	;
	
	private String id;
	private String text;
	
	private ClimateIndexEnum(String id, String text) {
		this.id = id;
		this.text = text;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
}
