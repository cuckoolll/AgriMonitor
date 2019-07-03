package com.agri.monitor.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.ClimateInfo;
import com.agri.monitor.entity.WaterInfo;
import com.agri.monitor.vo.ClimateQueryVO;

@Repository
public interface ClimateInfoMapper {
	
	List<WaterInfo> findAll();
	
	List<WaterInfo> queryInfoForPage(final ClimateQueryVO queryVo);
	
	void insertInfo(ClimateInfo climateinfo);
	
	void updateInfo(ClimateInfo climateinfo);
	
	String queryGid(final String county, final String date_year);

	void delInfoByGid(List<Integer> gids);

	ClimateInfo findById(Integer gid);
}
