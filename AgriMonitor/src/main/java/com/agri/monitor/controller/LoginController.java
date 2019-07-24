package com.agri.monitor.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.service.UserInfoService;

@Controller
public class LoginController {
	
	@Autowired
	private UserInfoService userInfoService;
	
	@ResponseBody
	@RequestMapping(value="/doLogin",method = RequestMethod.POST)
	public UserInfo doLogin(String userid, String pw,HttpServletRequest request) {
		return userInfoService.findForLogin(userid, pw, request);
	}
	
	@RequestMapping(value="/loginout")
	public String doLogin(HttpServletRequest request) {
		request.getSession().removeAttribute("userinfo");
		return "redirect:/login.html";
	}
}

