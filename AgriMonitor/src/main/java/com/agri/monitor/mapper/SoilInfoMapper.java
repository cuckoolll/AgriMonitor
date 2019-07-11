package com.agri.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.SoilInfo;
import com.agri.monitor.vo.SoilQueryVO;

@Repository
public interface SoilInfoMapper {
	
	List<SoilInfo> findAll();
	
	List<SoilInfo> queryInfoForPage(final SoilQueryVO queryVo);
	
	int queryInfoCount(final SoilQueryVO queryVo);
	
	void insertInfo(SoilInfo soilInfo);
	
	void updateInfo(SoilInfo soilInfo);
	
	String queryGid(final String date_year, final String code_number);

	void delInfoByGid(List<Integer> gids);

	SoilInfo findById(Integer gid);

	List<Map> queryAnalysisData(Map param);
}
