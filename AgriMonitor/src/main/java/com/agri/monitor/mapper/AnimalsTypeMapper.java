package com.agri.monitor.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.AnimalsType;
import com.agri.monitor.vo.AnimalsTypeQueryVO;

@Repository
public interface AnimalsTypeMapper {
	
	AnimalsType findById(Integer gid);
	
	List<AnimalsType> findAllForPage(AnimalsTypeQueryVO queryVO);
	
	int findAllCount(AnimalsTypeQueryVO queryVO);
	
	void batchInsert(List<AnimalsType> list);
	
	void insert(AnimalsType animalstype);
	
	void update(AnimalsType animalstype);
	
	void delete(List<Integer> gids);
	
	void animalstypeQy(List<Integer> gids);
	
	void animalstypeTy(List<Integer> gids);
	
}
