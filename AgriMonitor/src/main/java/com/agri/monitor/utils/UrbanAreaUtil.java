package com.agri.monitor.utils;

public class UrbanAreaUtil {
	private final static String[] towns = {"沙柳河镇", "哈尔盖镇", "伊克乌兰乡", "泉吉乡", "吉尔孟乡"}; 
	
	public static boolean isLegalTown(String town) {
		for(int i = 0; i < towns.length; i++) {
			if (towns[i].equals(town)) {
				return true;
			}
		}
		return false;
	}
}
