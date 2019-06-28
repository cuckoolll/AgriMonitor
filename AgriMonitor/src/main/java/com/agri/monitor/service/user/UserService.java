package com.agri.monitor.service.user;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.agri.monitor.dao.user.IUserDao;
import com.agri.monitor.entity.UserInfo;

@Service
public class UserService {
	@Autowired
	private IUserDao userDao;
	
	@Value("${spring.profiles.active}")  
	private String active;  
	 
	public boolean isFind(String name, String pw) {
		List<Map> list = userDao.findUserForLogin(name, pw);
		if (null != list && list.size() == 1) {
			return true;
		} 
		return false;
	}
	
	public List<UserInfo> findAll() {
		System.out.println(active);
		return userDao.findAll();
	}
}
