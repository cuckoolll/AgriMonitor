package com.agri.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.AgriInputinfo;
import com.agri.monitor.vo.AgriInputinfoQueryVO;

@Repository
public interface AgriInputinfoMapper {
	
	AgriInputinfo findById(Integer gid);
	
	List<Map> findAllForPage(AgriInputinfoQueryVO queryVO);
	
	int findAllCount(AgriInputinfoQueryVO queryVO);
	
	void insert(AgriInputinfo farmInfo);
	
	void update(AgriInputinfo farmInfo);
	
	void delete(List<Integer> gids);
	
	void deleteByTows(Map map);
	
}
