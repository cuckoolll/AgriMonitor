package com.agri.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.ProTeamAndEmployInfo;
import com.agri.monitor.vo.ProTeamAndEmployInfoQueryVO;

@Repository
public interface ProTeamAndEmployInfoMapper {
	
	ProTeamAndEmployInfo findById(Integer gid);
	
	List<Map> findAllForPage(ProTeamAndEmployInfoQueryVO queryVO);
	
	int findAllCount(ProTeamAndEmployInfoQueryVO queryVO);
	
	void batchInsert(List<ProTeamAndEmployInfo> list);
	
	void insert(ProTeamAndEmployInfo farmInfo);
	
	void update(ProTeamAndEmployInfo farmInfo);
	
	void delete(List<Integer> gids);
	
	void deleteByTows(Map map);
	
}
