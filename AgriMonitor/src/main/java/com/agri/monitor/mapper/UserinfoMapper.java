package com.agri.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.RoleInfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.vo.UserQueryVO;

@Repository
public interface UserinfoMapper {
	
	UserInfo findById(String userid);
	
	List<Map> findAllForPage(UserQueryVO queryVO);
	
	int findAllCount(UserQueryVO queryVO);
	
	void insert(UserInfo userInfo);
	
	void update(UserInfo userInfo);
	
	void delete(List<Integer> gids);
	
	List<Map> queryUserRole();
	
	UserInfo findForLogin(Map map);
	
	RoleInfo findRole(Integer gid);
}
