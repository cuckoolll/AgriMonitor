package com.agri.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.FishInfo;
import com.agri.monitor.vo.FishQueryVO;

@Repository
public interface FishInfoMapper {
	
	List<FishInfo> findAll();
	
	List<Map> queryInfoForPage(final FishQueryVO queryVo);
	
	int queryInfoCount(final FishQueryVO queryVo);
	
	void insertInfo(FishInfo fishinfo);
	
	void updateInfo(FishInfo fishinfo);
	
	String queryGid(final String county, final String date_year);

	void delInfoByGid(List<Integer> gids);

	FishInfo findById(Integer gid);

	List<Map> queryAnalysisData(Map param);
}
