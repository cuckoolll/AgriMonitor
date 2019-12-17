package com.agri.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.Gmhyzxx;
import com.agri.monitor.vo.GmhyzxxQueryVO;

@Repository
public interface GmhyzxxMapper {
	
	Gmhyzxx findById(Integer gid);
	
	List<Map> findAllForPage(GmhyzxxQueryVO queryVO);
	
	int findAllCount(GmhyzxxQueryVO queryVO);
	
	void insert(Gmhyzxx farmInfo);
	
	void update(Gmhyzxx farmInfo);
	
	void delete(List<Integer> gids);
	
	void deleteByTows(Map map);
	
	void batchInsert(List<Gmhyzxx> list);
}
