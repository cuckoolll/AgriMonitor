package com.agri.monitor.mapper.water;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.WaterInfo;

@Repository
public interface WaterInfoMapper {
	
	List<WaterInfo> findAll();
	
	List<WaterInfo> queryInfoByCountryAndTime(final String county, final String time);
}
