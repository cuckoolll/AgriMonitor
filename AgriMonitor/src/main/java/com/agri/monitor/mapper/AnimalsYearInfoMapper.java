package com.agri.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.AnimalsYearInfo;
import com.agri.monitor.vo.AnimalsYearQueryVO;

@Repository
public interface AnimalsYearInfoMapper {
	
	List<AnimalsYearInfo> findAll();
	
	List<Map> queryInfoForPage(final AnimalsYearQueryVO queryVo);
	
	int queryInfoCount(final AnimalsYearQueryVO queryVo);
	
	void insertInfo(AnimalsYearInfo animalsyearinfo);
	
	void updateInfo(AnimalsYearInfo animalsyearinfo);
	
	String queryGid(final String county, final String date_year);

	void delInfoByGid(List<Integer> gids);

	AnimalsYearInfo findById(Integer gid);

	List<Map> queryAnalysisData(Map param);
}
