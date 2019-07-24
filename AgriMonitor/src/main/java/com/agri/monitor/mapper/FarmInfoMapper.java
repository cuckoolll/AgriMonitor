package com.agri.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.FarmInfo;
import com.agri.monitor.vo.FarmQueryVO;

@Repository
public interface FarmInfoMapper {
	
	FarmInfo findById(Integer gid);
	
	List<Map> findAllForPage(FarmQueryVO farmQueryVO);
	
	int findAllCount(FarmQueryVO farmQueryVO);
	
	void batchInsert(List<FarmInfo> list);
	
	void insert(FarmInfo farmInfo);
	
	void update(FarmInfo farmInfo);
	
	void delete(List<Integer> gids);
	
	int findByAnimalsType(List<Integer> typeGids);
	
	List<Map> findSumByType(List<Integer> type);
	
	List<Map> findNumByType(List<Integer> type);
	
	List<Map> findSumGroupTowns();

}
