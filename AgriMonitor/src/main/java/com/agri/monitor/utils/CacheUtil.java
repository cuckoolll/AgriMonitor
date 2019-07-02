package com.agri.monitor.utils;

import java.util.HashMap;
import java.util.Map;

import com.agri.monitor.enums.CacheTypeEnum;

public class CacheUtil {
	private CacheUtil() {}
	
	private static Map<String, Object> map = new HashMap<>();
	
	public static Object getCache(CacheTypeEnum e) {
		return map.get(e.toString());
	}
	
	public static void putCache(CacheTypeEnum e, Object o) {
		if (null != o) {
			map.put(e.toString(), o);
		}
	}
}
