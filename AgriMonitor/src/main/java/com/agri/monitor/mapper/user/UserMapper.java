package com.agri.monitor.mapper.user;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.UserInfo;

@Repository
public interface UserMapper {

    List<UserInfo> findAll();
	
}
