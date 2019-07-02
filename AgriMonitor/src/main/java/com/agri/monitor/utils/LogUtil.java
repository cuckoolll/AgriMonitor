package com.agri.monitor.utils;

import com.agri.monitor.entity.SystemLog;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.service.sysset.SystemLogService;

public class LogUtil {
	private LogUtil() {}
	public static void log(LogOptTypeEnum type,LogOptSatusEnum status,Integer userid, String log) {
		SystemLogService service = SpringUtil.getObject(SystemLogService.class);
		SystemLog entity = new SystemLog();
		entity.setOperation_type(type.getTypeId());
		entity.setOperation_status(status.ordinal());
		entity.setOperation_log(log);
		entity.setOperation_user(userid);
		service.insert(entity);
	}
}
