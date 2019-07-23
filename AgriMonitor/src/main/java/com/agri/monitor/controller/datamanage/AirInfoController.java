package com.agri.monitor.controller.datamanage;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.agri.monitor.annotation.IgnoreSession;
import com.agri.monitor.entity.AirInfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.service.datamanage.AirInfoService;
import com.agri.monitor.vo.AirQueryVO;

@Controller
@RequestMapping("/airinfo")
public class AirInfoController {
	
	@Autowired
	private AirInfoService airInfoService;
	
	@RequestMapping("")
	public String airinfo() {
		return "/datamanage/airinfo/airinfo";
	}

	@RequestMapping(value="/queryInfo", method = RequestMethod.POST)
	@ResponseBody
	@IgnoreSession
	public Map queryInfo(AirQueryVO queryVo, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return airInfoService.queryInfoByCountryAndTimeForPage(queryVo, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/dataImport",method=RequestMethod.POST)
	public Map dataImport(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		return airInfoService.dataImport(file, request);
	}
	
	@IgnoreSession
	@RequestMapping("/update")
	public String add(Model model) {
		return "/datamanage/airinfo/airupdate";
	}

	@ResponseBody
	@RequestMapping(value="/save",method=RequestMethod.POST)
	public Map doUpdate(AirInfo airinfo,HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return airInfoService.saveOrUpdate(airinfo, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/delInfoByGid",method=RequestMethod.POST)
	public Map delInfoByGid(@RequestBody ArrayList<Integer> gids, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return airInfoService.delInfoByGid(gids, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/findById",method=RequestMethod.POST)
	public AirInfo findById(Integer gid, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return airInfoService.findById(gid, user.getUser_id());
	}
}
