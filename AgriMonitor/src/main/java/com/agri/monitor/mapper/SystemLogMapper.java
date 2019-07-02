package com.agri.monitor.mapper;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.SystemLog;

@Repository
public interface SystemLogMapper {
	
	void insert(SystemLog log);
	
}
