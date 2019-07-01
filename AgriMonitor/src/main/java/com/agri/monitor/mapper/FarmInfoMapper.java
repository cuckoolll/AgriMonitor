package com.agri.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.FarmInfo;

@Repository
public interface FarmInfoMapper {
	
	FarmInfo findById(Integer gid);
	
	List<Map> findAll(FarmInfo farminfo);
	
	void batchInsert(List<FarmInfo> list);
	
	void insert(FarmInfo farmInfo);
	
	void update(FarmInfo farmInfo);
}
