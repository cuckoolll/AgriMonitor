package com.agri.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.SystemLog;
import com.agri.monitor.vo.SysLogQueryVO;

@Repository
public interface SystemLogMapper {
	
	void insert(SystemLog log);
	
	List<Map> findAllForPage(SysLogQueryVO queryVO);
	
	int findAllCount(SysLogQueryVO queryVO);
	
	void delete(List<Integer> gids);
}
