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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.agri.monitor.annotation.IgnoreSession;
import com.agri.monitor.entity.FarmInfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.entity.WaterInfo;
import com.agri.monitor.enums.CacheTypeEnum;
import com.agri.monitor.service.datamanage.WaterInfoService;
import com.agri.monitor.utils.CacheUtil;
import com.agri.monitor.vo.WaterQueryVO;

@Controller
@RequestMapping("/waterinfo")
public class WaterInfoController {
	
	@Autowired
	private WaterInfoService waterInfoService;
	
	/**
	 * 水质监测页面 .
	 * @return .
	 */
	@RequestMapping("")
	public String waterMonitor() {
		return "/datamanage/waterinfo/waterinfo";
	}
	
	/**
	 * 查询水质监测信息 .
	 * @param request .
	 * @return .
	 */
	@RequestMapping(value="/queryWaterInfo", method = RequestMethod.POST)
	@ResponseBody
	@IgnoreSession
	public Map queryWaterInfo(WaterQueryVO queryVo, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return waterInfoService.queryInfoByCountryAndTimeForPage(queryVo, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/dataImport",method=RequestMethod.POST)
	public Map dataImport(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		return waterInfoService.dataImport(file, request);
	}
	
	@IgnoreSession
	@RequestMapping("/update")
	public String add(Model model) {
		return "/datamanage/waterinfo/update";
	}

	@ResponseBody
	@RequestMapping(value="/save",method=RequestMethod.POST)
	public Map doUpdate(WaterInfo waterinfo,HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return waterInfoService.saveOrUpdate(waterinfo, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/delInfoByGid",method=RequestMethod.POST)
	public Map delInfoByGid(@RequestBody ArrayList<Integer> gids, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return waterInfoService.delInfoByGid(gids, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/findById",method=RequestMethod.POST)
	public WaterInfo findById(Integer gid, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return waterInfoService.findById(gid, user.getUser_id());
	}
}
