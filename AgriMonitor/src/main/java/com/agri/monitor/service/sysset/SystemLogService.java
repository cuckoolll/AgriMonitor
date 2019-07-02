package com.agri.monitor.service.sysset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agri.monitor.entity.SystemLog;
import com.agri.monitor.mapper.SystemLogMapper;

@Service
public class SystemLogService {
	@Autowired
	private SystemLogMapper systemLogMapper;
	
	public void insert(SystemLog log) {
		systemLogMapper.insert(log);
	}
}
