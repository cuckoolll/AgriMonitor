package com.agri.monitor.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.AnimalsTarget;
import com.agri.monitor.vo.AnimalsTargetQueryVO;

@Repository
public interface AnimalsTargetMapper {
	
	AnimalsTarget findById(Integer gid);
	
	List<AnimalsTarget> findAllForPage(AnimalsTargetQueryVO queryVO);
	
	int findAllCount(AnimalsTargetQueryVO queryVO);
	
	void insert(AnimalsTarget animalstype);
	
	void update(AnimalsTarget animalstype);
	
	void delete(List<Integer> gids);
	
	void animalsTargetQy(List<Integer> gids);
	
	void animalsTargetTy(List<Integer> gids);
	
}
