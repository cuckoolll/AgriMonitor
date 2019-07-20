package com.agri.monitor.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.WaterInfo;
import com.agri.monitor.vo.WaterQueryVO;

@Repository
public interface WaterInfoMapper {
	
	List<WaterInfo> findAll();
	
	List<WaterInfo> queryInfoByCountryAndTimeForPage(final WaterQueryVO queryVo);
	
	int queryInfoCount(final WaterQueryVO queryVo);
	
	void insertWaterInfo(WaterInfo waterinfo);
	
	void updateWaterInfo(WaterInfo waterinfo);
	
	String queryGid(final String quality_address, final String quality_time, final String quality_type);

	void delInfoByGid(List<Integer> gids);

	WaterInfo findById(Integer gid);
}
