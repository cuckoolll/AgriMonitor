package com.agri.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.CropsPlant;
import com.agri.monitor.vo.CropsPlantQueryVO;

@Repository
public interface CropsPlantMapper {
	
	CropsPlant findById(Integer gid);
	
	List<Map> findAllForPage(CropsPlantQueryVO queryVO);
	
	int findAllCount(CropsPlantQueryVO queryVO);
	
	void batchInsert(List<CropsPlant> list);
	
	void insert(CropsPlant info);
	
	void update(CropsPlant info);
	
	void delete(List<Integer> gids);
	
	int findByCropsType(List<Integer> typeGids);
	
	void deleteByYear(Integer year);
	
	void deleteByYearAndType(Map map);
	
	List<Map> getdata10(Integer type);
}
