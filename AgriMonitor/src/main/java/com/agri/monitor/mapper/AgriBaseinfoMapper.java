package com.agri.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.AgriBaseinfo;
import com.agri.monitor.vo.AgriBaseinfoQueryVO;

@Repository
public interface AgriBaseinfoMapper {
	
	AgriBaseinfo findById(Integer gid);
	
	List<Map> findAllForPage(AgriBaseinfoQueryVO queryVO);
	
	int findAllCount(AgriBaseinfoQueryVO queryVO);
	
	void batchInsert(List<AgriBaseinfo> list);
	
	void insert(AgriBaseinfo farmInfo);
	
	void update(AgriBaseinfo farmInfo);
	
	void delete(List<Integer> gids);
	
	void deleteByTows(Map map);
	
}
