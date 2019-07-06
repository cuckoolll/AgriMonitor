package com.agri.monitor.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.agri.monitor.entity.RoleInfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.service.LoginService;

@Controller
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	
	@ResponseBody
	@RequestMapping(value="/doLogin",method = RequestMethod.POST)
	public Map doLogin(String username,String password,HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserInfo user = new UserInfo();
		RoleInfo role = new RoleInfo();
		role.setGid(1);
		role.setRole_name("admin");
		role.setRole_showname("管理员");
		user.setUser_id("zhangsan");
		user.setUser_showname("张三111");
		user.setRoleinfo(role);
		session.setAttribute("userinfo", user);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		result.put("msg", "成功");
		return result;
	}
}

