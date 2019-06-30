package com.agri.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.FarmInfo;

@Repository
public interface FarmInfoMapper {
	
	List<Map> findAll(Map map);
	
	void batchInsert(List<FarmInfo> list);
}
