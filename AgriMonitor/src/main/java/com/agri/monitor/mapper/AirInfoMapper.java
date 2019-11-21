package com.agri.monitor.mapper;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.AirInfo;
import com.agri.monitor.vo.AirQueryVO;

@Repository
public interface AirInfoMapper {
	
	List<AirInfo> findAll();
	
	List<AirInfo> queryInfoByCountryAndTimeForPage(final AirQueryVO queryVo);
	
	int queryInfoCount(final AirQueryVO queryVo);
	
	void insertInfo(AirInfo airinfo);
	
	void updateInfo(AirInfo airinfo);
	
	String queryGid(/*final String city, final String station_name,*/ final String quality_time);

	void delInfoByGid(List<Integer> gids);

	AirInfo findById(Integer gid);
}
