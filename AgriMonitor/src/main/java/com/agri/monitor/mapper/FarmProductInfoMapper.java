package com.agri.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.FarmProductInfo;
import com.agri.monitor.entity.FishInfo;
import com.agri.monitor.vo.FarmProductQueryVO;

@Repository
public interface FarmProductInfoMapper {
	
	List<FishInfo> findAll();
	
	List<FishInfo> queryInfoForPage(final FarmProductQueryVO queryVo);
	
	int queryInfoCount(final FarmProductQueryVO queryVo);
	
	void insertInfo(FarmProductInfo farmproductinfo);
	
	void updateInfo(FarmProductInfo farmproductinfo);
	
	String queryGid(final String county, final String date_year);

	void delInfoByGid(List<Integer> gids);

	FarmProductInfo findById(Integer gid);

	List<Map> queryAnalysisData(Map param);
}
