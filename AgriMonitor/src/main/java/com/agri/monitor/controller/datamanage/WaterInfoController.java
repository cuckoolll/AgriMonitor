package com.agri.monitor.controller.datamanage;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.agri.monitor.annotation.IgnoreSession;
import com.agri.monitor.service.datamanage.WaterInfoService;

@Controller
@RequestMapping("/water")
public class WaterInfoController {
	
	@Autowired
	private WaterInfoService waterInfoService;
	
	/**
	 * 水质监测页面 .
	 * @return .
	 */
	@RequestMapping("/waterinfo.html")
	public String waterMonitor() {
		return "/waterinfo/waterinfo";
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
}
