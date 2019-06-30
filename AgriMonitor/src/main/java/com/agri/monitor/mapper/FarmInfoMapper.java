package com.agri.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface FarmInfoMapper {
	
	List<Map> findAll(Map map);
	
}
