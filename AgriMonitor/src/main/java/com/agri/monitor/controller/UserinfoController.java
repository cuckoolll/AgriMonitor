package com.agri.monitor.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.agri.monitor.annotation.IgnoreSession;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.CacheTypeEnum;
import com.agri.monitor.service.UserInfoService;
import com.agri.monitor.utils.CacheUtil;
import com.agri.monitor.vo.UserQueryVO;

@Controller
@RequestMapping("/userinfo")
public class UserinfoController {
	
	@Autowired
	private UserInfoService userInfoService;
	
	@RequestMapping("")
	public String toPage(Model model) {
		model.addAttribute("user_role", CacheUtil.getCache(CacheTypeEnum.USER_ROLE));
		return "/userinfo/datalist";
	}
	
	@IgnoreSession
	@RequestMapping("update")
	public String add(Model model) {
		model.addAttribute("user_role", CacheUtil.getCache(CacheTypeEnum.USER_ROLE));
		return "/userinfo/update";
	}
	@RequestMapping("updpw")
	public String updpw(Model model) {
		return "/userinfo/updpw";
	}
	
	@ResponseBody
	@RequestMapping("savepw")
	public boolean savepw(UserInfo userinfo, HttpServletRequest request) {
		return userInfoService.savepw(userinfo, request);
	}
	
	@ResponseBody
	@RequestMapping(value="/doDel",method=RequestMethod.POST)
	public Map doDel(@RequestBody ArrayList<Integer> gids, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return userInfoService.doDel(gids, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/doAdd",method=RequestMethod.POST)
	public Map doAdd(UserInfo userinfo,HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return userInfoService.saveOrUpdate(userinfo,1,user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/doUpdate",method=RequestMethod.POST)
	public Map doUpdate(UserInfo userinfo,HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return userInfoService.saveOrUpdate(userinfo,2,user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/findById",method=RequestMethod.POST)
	public UserInfo findById(String gid, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return userInfoService.findById(gid, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/datalist",method=RequestMethod.POST)
	public Map datalist(UserQueryVO queryVO, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		result.put("msg", "成功");
		result.put("data", userInfoService.findAllForPage(queryVO, user.getUser_id()));
		result.put("count", userInfoService.findAllCount(queryVO));
		return result;
	}
	
}

