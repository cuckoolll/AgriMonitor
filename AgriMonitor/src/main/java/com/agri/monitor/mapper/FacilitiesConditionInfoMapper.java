package com.agri.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.FacilityConditionInfo;
import com.agri.monitor.vo.FacilityConditionQueryVO;

@Repository
public interface FacilitiesConditionInfoMapper {
	
	List<FacilityConditionInfo> findAll();
	
	List<Map> queryInfoForPage(final FacilityConditionQueryVO queryVo);
	
	int queryInfoCount(final FacilityConditionQueryVO queryVo);
	
	void insertInfo(FacilityConditionInfo facilityconditioninfo);
	
	void updateInfo(FacilityConditionInfo facilityconditioninfo);
	
	String queryGid(final String county, final String date_year);

	void delInfoByGid(List<Integer> gids);

	FacilityConditionInfo findById(Integer gid);

	List<Map> queryAnalysisData(Map param);
}
