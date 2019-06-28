package com.agri.monitor.dao.user;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.agri.monitor.entity.UserInfo;

public interface IUserDao extends JpaRepository<UserInfo, Integer> {
	
	@Query(value="select user_id,user_name,user_password from t_user_info where user_name=?1 and user_password=?2", nativeQuery = true)
	List<Map> findUserForLogin(String username, String pw);
	
	@Query("FROM UserInfo")
    List<UserInfo> findAll();
}
