package com.agri.monitor.utils;

import java.util.Arrays;
import java.util.List;

public class UrbanAreaUtil {
	private final static List<String> towns =  Arrays.asList(new String[]{"沙柳河镇","哈尔盖镇","伊克乌兰乡","泉吉乡","吉尔孟乡"}); 
	
	public static boolean isLegalTown(String town) {
		if (towns.contains(town)) {
			return true;
		}
		return false;
	}
}
