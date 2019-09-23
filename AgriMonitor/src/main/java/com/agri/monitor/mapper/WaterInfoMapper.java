package com.agri.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.WaterInfo;
import com.agri.monitor.entity.WaterInfoRowFixed;
import com.agri.monitor.vo.WaterQueryVO;

@Repository
public interface WaterInfoMapper {
	
	List<WaterInfo> findAll();
	
	List<Map> queryInfoByCountryAndTimeForPage(final WaterQueryVO queryVo);
	
	List<Map> queryInfoByCountryAndTime(final WaterQueryVO queryVo);
	
	List<Map> queryInfoRowFixed(final WaterQueryVO queryVo);
	
	int queryInfoCount(final WaterQueryVO queryVo);
	
	int queryInfoCountRowFixed(final WaterQueryVO queryVo);
	
	void insertWaterInfo(WaterInfo waterinfo);
	
	void insertWaterInfoRowFixed(WaterInfoRowFixed waterinfo);
	
	void updateWaterInfo(WaterInfo waterinfo);
	
	void updateWaterInfoRowFixed(WaterInfoRowFixed waterinfo);
	
	String queryGid(final String quality_address, final String quality_time, final String quality_type);

	void delInfoByGid(List<Integer> gids);

	void delInfoByGidRowFixed(List<Integer> gids);
	
	WaterInfo findById(Integer gid);
	
	WaterInfoRowFixed findByIdRowFixed(Integer gid);
	
	List<String> queryQualityType(final WaterQueryVO queryVo);
}
