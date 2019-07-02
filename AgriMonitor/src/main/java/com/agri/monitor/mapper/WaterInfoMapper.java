package com.agri.monitor.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.WaterInfo;

@Repository
public interface WaterInfoMapper {
	
	List<WaterInfo> findAll();
	
	List<WaterInfo> queryInfoByCountryAndTime(final String county, final String time);
	
	void insertWaterInfo(WaterInfo waterinfo);
	
	void updateWaterInfo(WaterInfo waterinfo);
	
	String queryGid(final String county, final String time, final String type);
}
