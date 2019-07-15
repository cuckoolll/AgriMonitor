package com.agri.monitor.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.PolicyInfo;
import com.agri.monitor.entity.WaterInfo;
import com.agri.monitor.vo.PolicyQueryVO;
import com.agri.monitor.vo.WaterQueryVO;

@Repository
public interface PolicyMaintainMapper {
	List<PolicyInfo> queryInfoForPage(final PolicyQueryVO queryVo);
	
	int queryInfoCount(final PolicyQueryVO queryVo);
	
	void insertInfo(PolicyInfo policyInfo);
	
	String queryGid(final String file_name);

	void delInfoByGid(List<Integer> gids);
}
