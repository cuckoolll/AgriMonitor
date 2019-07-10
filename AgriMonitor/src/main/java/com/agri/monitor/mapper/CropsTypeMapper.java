package com.agri.monitor.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.CropsType;
import com.agri.monitor.vo.CropsTypeQueryVO;

@Repository
public interface CropsTypeMapper {
	
	CropsType findById(Integer gid);
	
	List<CropsType> findAllForPage(CropsTypeQueryVO queryVO);
	
	int findAllCount(CropsTypeQueryVO queryVO);
	
	void insert(CropsType animalstype);
	
	void update(CropsType animalstype);
	
	void delete(List<Integer> gids);
	
	void cropstypeQy(List<Integer> gids);
	
	void cropstypeTy(List<Integer> gids);
	
}
