package com.agri.monitor.controller.datamanage;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.agri.monitor.annotation.IgnoreSession;
import com.agri.monitor.enums.CacheTypeEnum;
import com.agri.monitor.service.datamanage.WaterInfoService;
import com.agri.monitor.utils.CacheUtil;

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
	public Map quertWaterInfo(HttpServletRequest request) {
		final String county = request.getParameter("county");
		final String time = request.getParameter("time");
		
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		result.put("msg", "成功");
		result.put("data", waterInfoService.queryInfoByCountryAndTime(county, time));
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="/dataImport",method=RequestMethod.POST)
	public Map dataImport(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		return waterInfoService.dataImport(file, request);
	}
	
	@IgnoreSession
	@RequestMapping("add")
	public String add(Model model) {
		return "/datamanage/waterinfo/add";
	}
	
}
