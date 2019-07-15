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

import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.service.sysset.SystemLogService;
import com.agri.monitor.vo.SysLogQueryVO;

@Controller
@RequestMapping("/syslog")
public class SystemLogController {
	
	@Autowired
	private SystemLogService systemLogService;
	
	@RequestMapping("")
	public String toPage(Model model) {
		model.addAttribute("logopttype", LogOptTypeEnum.toList());
		return "/syslog/datalist";
	}
	
	@ResponseBody
	@RequestMapping(value="/datalist",method=RequestMethod.POST)
	public Map datalist(SysLogQueryVO queryVO, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		result.put("msg", "成功");
		result.put("data", systemLogService.findAllForPage(queryVO, user.getUser_id()));
		result.put("count", systemLogService.findAllCount(queryVO));
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="/doDel",method=RequestMethod.POST)
	public Map doDel(@RequestBody ArrayList<Integer> gids, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return systemLogService.doDel(gids, user.getUser_id());
	}
}

