package com.agri.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.GrassInfo;
import com.agri.monitor.vo.GrassQueryVO;

@Repository
public interface GrassInfoMapper {
	
	List<GrassInfo> findAll();
	
	List<Map> queryInfoForPage(final GrassQueryVO queryVo);
	
	int queryInfoCount(final GrassQueryVO queryVo);
	
	void insertInfo(GrassInfo grassinfo);
	
	void updateInfo(GrassInfo grassinfo);
	
	String queryGid(final String county, final String date_year);

	void delInfoByGid(List<Integer> gids);

	GrassInfo findById(Integer gid);

	List<Map> queryAnalysisData(Map param);
}
