package com.agri.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.NmshInfo;
import com.agri.monitor.vo.NmshInfoQueryVO;

@Repository
public interface NmshInfoMapper {
	
	NmshInfo findById(Integer gid);
	
	List<Map> findAllForPage(NmshInfoQueryVO queryVO);
	
	int findAllCount(NmshInfoQueryVO queryVO);
	
	void insert(NmshInfo farmInfo);
	
	void update(NmshInfo farmInfo);
	
	void delete(List<Integer> gids);
	
	void deleteByTows(Map map);
	
	void batchInsert(List<NmshInfo> list);
}
