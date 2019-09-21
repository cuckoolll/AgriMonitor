package com.agri.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.PolicyInfo;
import com.agri.monitor.vo.PolicyQueryVO;

@Repository
public interface PolicyMaintainMapper {
	List<Map> queryInfoForPage(final PolicyQueryVO queryVo);
	
	int queryInfoCount(final PolicyQueryVO queryVo);
	
	void insertInfo(PolicyInfo policyInfo);
	
	String queryGid(final String file_name);

	void delInfoByGid(List<Integer> gids);
}
