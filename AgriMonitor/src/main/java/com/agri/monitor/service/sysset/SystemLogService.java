package com.agri.monitor.service.sysset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agri.monitor.entity.SystemLog;
import com.agri.monitor.mapper.SystemLogMapper;
import com.agri.monitor.vo.SysLogQueryVO;

@Service
public class SystemLogService {
	@Autowired
	private SystemLogMapper systemLogMapper;
	
	private static final Logger logger = LoggerFactory.getLogger(SystemLogService.class);
	
	public void insert(SystemLog log) {
		systemLogMapper.insert(log);
	}
	
	public Map doDel(List<Integer> gids, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("系统操作日志删除开始：" + gids);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			systemLogMapper.delete(gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("系统操作日志删除异常" + e);
		}
		return result;
	}
	
	public List<Map> findAllForPage(SysLogQueryVO queryVO, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("查询所有系统日志开始：" + queryVO);
		}
		return systemLogMapper.findAllForPage(queryVO);
	}
	
	public int findAllCount(SysLogQueryVO queryVO) {
		return systemLogMapper.findAllCount(queryVO);
	}
}
