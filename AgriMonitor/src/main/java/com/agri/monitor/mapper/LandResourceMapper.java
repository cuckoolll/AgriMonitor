package com.agri.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.LandResource;
import com.agri.monitor.vo.LandResourceQueryVO;

@Repository
public interface LandResourceMapper {
	
	LandResource findById(Integer gid);
	
	List<Map> findAllForPage(LandResourceQueryVO queryVO);
	
	int findAllCount(LandResourceQueryVO queryVO);
	
	void insert(LandResource farmInfo);
	
	void update(LandResource farmInfo);
	
	void delete(List<Integer> gids);
	
	void deleteByTows(Map map);
	
	String queryGid(final int syear);
}
