package com.agri.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.AnimalsTarget;
import com.agri.monitor.vo.AnimalsTargetQueryVO;

@Repository
public interface AnimalsTargetMapper {
	
	AnimalsTarget findById(Integer gid);
	
	List<AnimalsTarget> findAllForPage(AnimalsTargetQueryVO queryVO);
	
	int findAllCount(AnimalsTargetQueryVO queryVO);
	
	int findCountByPId(Integer pid);
	
	void insert(AnimalsTarget animalstype);
	
	void update(AnimalsTarget animalstype);
	
	void delete(Integer gid);
	
	void animalsTargetQy(List<Integer> gids);
	
	void animalsTargetTy(List<Integer> gids);
	
	void updateIsLeaf(Map map);
	
	void updStatusByPid(Map map);
	
}
