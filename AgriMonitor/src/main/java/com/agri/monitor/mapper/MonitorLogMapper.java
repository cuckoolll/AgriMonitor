package com.agri.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.MonitorLog;
import com.agri.monitor.vo.MonitorSetQueryVO;

@Repository
public interface MonitorLogMapper {
	
	List<Map> findAllForPage(MonitorSetQueryVO queryVO);
	
	int findAllCount(MonitorSetQueryVO queryVO);
	
	void insert(MonitorLog monitorLog);
	
	void delete(List<Integer> gids);
	
	void deleteByType(List<Integer> setgid);
	
	void updateStatus(Map map);
	
	void updateStatusByType(Map map);
	void updateStatusAll();
	void deleteAll();
}
