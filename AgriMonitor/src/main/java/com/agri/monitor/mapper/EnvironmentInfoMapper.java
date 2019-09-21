package com.agri.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.EnvironmentInfo;
import com.agri.monitor.vo.EnvironmentQueryVO;

@Repository
public interface EnvironmentInfoMapper {
	
	List<EnvironmentInfo> findAll();
	
	List<Map> queryInfoForPage(final EnvironmentQueryVO queryVo);
	
	int queryInfoCount(final EnvironmentQueryVO queryVo);
	
	void insertInfo(EnvironmentInfo environmentinfo);
	
	void updateInfo(EnvironmentInfo environmentinfo);
	
	String queryGid(final String county, final String date_year);

	void delInfoByGid(List<Integer> gids);

	EnvironmentInfo findById(Integer gid);

	List<Map> queryAnalysisData(Map param);
}
