package com.agri.monitor.controller.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.agri.monitor.annotation.IgnoreSession;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.service.user.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService loginService;
	
	@RequestMapping("/userlist1")
	public String userlist1(Model model) {
		return "user/userlist1";
	}
	
	@RequestMapping("/findAll")
	@IgnoreSession
	public String findAll(UserInfo user, Model model) {
		List<UserInfo> list = loginService.findAll();
		model.addAttribute("list", list);
		return "user/userlist";
	}
	
	@ResponseBody
	@RequestMapping("/findAll1")
	@IgnoreSession
	public Map findAll1(Model model) {
		Map m = new HashMap();
		m.put("code", 0);
		m.put("msg", "成功");
		m.put("data", loginService.findAll());
		return m;
	}
}
