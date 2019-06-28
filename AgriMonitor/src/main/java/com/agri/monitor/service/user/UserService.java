package com.agri.monitor.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.mapper.user.UserMapper;

@Service
public class UserService {
	@Autowired
	private UserMapper userMapper;
	
	@Value("${spring.profiles.active}")  
	private String active;  
	 
	public List<UserInfo> findAll() {
		return userMapper.findAll();
	}
}
