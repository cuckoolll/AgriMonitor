package com.agri.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.MonitorSet;
import com.agri.monitor.vo.MonitorSetQueryVO;

@Repository
public interface MonitorSetMapper {
	
	MonitorSet findById(Integer gid);
	
	List<Map> findAllForPage(MonitorSetQueryVO queryVO);
	
	int findAllCount(MonitorSetQueryVO queryVO);
	
	void insert(MonitorSet monitorSet);
	
	void update(MonitorSet monitorSet);
	
	void delete(List<Integer> gids);
}
