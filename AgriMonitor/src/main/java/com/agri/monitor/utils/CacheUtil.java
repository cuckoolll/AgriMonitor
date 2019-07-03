package com.agri.monitor.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agri.monitor.enums.CacheTypeEnum;

public class CacheUtil {
	private static final Logger logger = LoggerFactory.getLogger(CacheUtil.class);
	private CacheUtil() {}
	
	private static Map<String, Object> map = new HashMap<>();
	
	public static Object getCache(CacheTypeEnum e) {
		return map.get(e.toString());
	}
	
	public static void putCache(CacheTypeEnum e, Object o) {
		if (null != o) {
			map.put(e.toString(), o);
			if (logger.isInfoEnabled()) {
				logger.info("更新"+e.toString()+"缓存完成..........");
			}
		}
	}
}
