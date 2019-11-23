package com.agri.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.Areainfo;

@Repository
public interface AreainfoMapper {
	
	Areainfo findById(Integer gid);
	
	List<Map> findAll();
	
	void update(Areainfo areainfo);
	
}
