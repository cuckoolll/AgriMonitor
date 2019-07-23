package com.agri.monitor.enums;

public enum GrassIndexEnum {
	
	GRASS_AREA("grass_area", "草场总面积（万亩）"),
	GRASS_USABLE_AREA("grass_usable_area", "草场可用面积（万亩）"),
	GRASS_RETAIN_AREA("grass_retain_area", "多年生人工种草保留面积（万亩）"),
	GRASS_UNFORAGE("grass_unforage", "禁牧（kg/hm2）"),
	GRASS_ANIMAL_BALANCE("grass_animal_balance", "草畜平衡（kg/hm2）"),
	PLATEAU_PIKA_AREA("plateau_pika_area", "高原鼠兔危害面积（万亩）"),
	PLATEAU_ZOKOR_AREA("plateau_zokor_area", "高原鼢鼠危害面积（万亩）"),
	GRASS_WORM_AREA("grass_worm_area", "草原毛虫危害面积（万亩）"),
	GRASSHOPPER_AREA("grasshopper_area", "蝗虫危害面积（万亩）"),
	;
	
	private String id;
	private String text;
	
	private GrassIndexEnum(String id, String text) {
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
