package com.agri.monitor.enums;

public enum SoilIndexEnum {
	
	ORGANIC("organic", "有机质（g/kg）"),
	NITROGEN("nitrogen", "全氮（g/kg）"),
	PHOSPHORUS("phosphorus", "全磷含量(g/kg)"),
	EFFECTIVE_PHOSPHORUS("effective_phosphorus", "有效磷含量(mg/kg)"),
	POTASSIUM("potassium", "全钾含量(g/kg)"),
	PH("ph", "pH"),
	SALINITY("salinity", "盐度（%）"),
	AVAILABLE_POTASSIUM("available_potassium", "速效钾含量(mg/kg)"),
	SLOW_RELEASE_POTASSIUM("slow_release_potassium", "缓效钾含量(mg/kg)"),
	;
	
	private String id;
	private String text;
	
	private SoilIndexEnum(String id, String text) {
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
