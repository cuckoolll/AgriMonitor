package com.agri.monitor.controller.datamanage;

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
import com.agri.monitor.entity.ProTeamAndEmployInfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.CacheTypeEnum;
import com.agri.monitor.service.datamanage.ProTeamAndEmployInfoService;
import com.agri.monitor.utils.CacheUtil;
import com.agri.monitor.vo.ProTeamAndEmployInfoQueryVO;

@Controller
@RequestMapping("/proteamandemployinfo")
public class ProTeamAndEmployInfoController {
	
	@Autowired
	private ProTeamAndEmployInfoService proTeamAndEmployInfoService;
	
	@RequestMapping("")
	public String proTeamAndEmployInfo(Model model) {
		model.addAttribute("towns", CacheUtil.getCache(CacheTypeEnum.TOWNS));
		return "/datamanage/proteamandemployinfo/datalist";
	}
	
	@IgnoreSession
	@RequestMapping("update")
	public String add(Model model) {
		model.addAttribute("towns", CacheUtil.getCache(CacheTypeEnum.TOWNS));
		return "/datamanage/proteamandemployinfo/update";
	}
	
	@ResponseBody
	@RequestMapping(value="/doDel",method=RequestMethod.POST)
	public Map doDel(@RequestBody ArrayList<Integer> gids, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return proTeamAndEmployInfoService.doDel(gids, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/doSave",method=RequestMethod.POST)
	public Map doUpdate(ProTeamAndEmployInfo info,HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return proTeamAndEmployInfoService.saveOrUpdate(info,user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/findById",method=RequestMethod.POST)
	public ProTeamAndEmployInfo findById(Integer gid, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return proTeamAndEmployInfoService.findById(gid, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/datalist",method=RequestMethod.POST)
	public Map datalist(ProTeamAndEmployInfoQueryVO queryVO, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		result.put("msg", "成功");
		result.put("data", proTeamAndEmployInfoService.findAllForPage(queryVO, user.getUser_id()));
		result.put("count", proTeamAndEmployInfoService.findAllCount(queryVO));
		return result;
	}
	
}
