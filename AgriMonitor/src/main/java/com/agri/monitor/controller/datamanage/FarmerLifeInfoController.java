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
import com.agri.monitor.entity.NmshInfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.service.datamanage.NmshInfoService;
import com.agri.monitor.vo.NmshInfoQueryVO;

@Controller
@RequestMapping("/farmerlifeinfo")
public class FarmerLifeInfoController {
	
	@Autowired
	private NmshInfoService nmshInfoService;
	
	@RequestMapping("")
	public String farmerLifeInfo(Model model) {
		return "/datamanage/farmerlifeinfo/farmerlifeinfo";
	}
	
	@IgnoreSession
	@RequestMapping("update")
	public String add(Model model) {
		return "/datamanage/farmerlifeinfo/update";
	}
	
	@ResponseBody
	@RequestMapping(value="/doDel",method=RequestMethod.POST)
	public Map doDel(@RequestBody ArrayList<Integer> gids, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return nmshInfoService.doDel(gids, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/doSave",method=RequestMethod.POST)
	public Map doUpdate(NmshInfo info,HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return nmshInfoService.saveOrUpdate(info,user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/findById",method=RequestMethod.POST)
	public NmshInfo findById(Integer gid, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return nmshInfoService.findById(gid, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/datalist",method=RequestMethod.POST)
	public Map datalist(NmshInfoQueryVO queryVO, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		result.put("msg", "成功");
		result.put("data", nmshInfoService.findAllForPage(queryVO, user.getUser_id()));
		result.put("count", nmshInfoService.findAllCount(queryVO));
		return result;
	}
}
