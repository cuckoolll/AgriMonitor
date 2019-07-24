package com.agri.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.AnimalsBreed;
import com.agri.monitor.vo.AnimalsBreedQueryVO;

@Repository
public interface AnimalsBreedMapper {
	
	AnimalsBreed findById(Integer gid);
	
	List<Map> findAllForPage(AnimalsBreedQueryVO queryVO);
	
	int findAllCount(AnimalsBreedQueryVO queryVO);
	
	void batchInsert(List<AnimalsBreed> list);
	
	void insert(AnimalsBreed farmInfo);
	
	void update(AnimalsBreed farmInfo);
	
	void delete(List<Integer> gids);
	
	int findByAnimalsTarget(Integer typeGid);
	
	void deleteByTowns(Map map);
	
	List<Map> getYearData(Map map);
	
	List<Map> getSumGroupYear(Map map);
}
