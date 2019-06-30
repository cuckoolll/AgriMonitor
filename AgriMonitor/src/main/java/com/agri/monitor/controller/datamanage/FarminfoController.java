package com.agri.monitor.controller.datamanage;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.agri.monitor.service.datamanage.FarmInfoService;

@Controller
@RequestMapping("/farminfo")
public class FarminfoController {
	
	@Autowired
	private FarmInfoService farmInfoService;
	
	@RequestMapping("")
	public String toPage() {
		return "/datamanage/farminfo/datalist";
	}
	
	@ResponseBody
	@RequestMapping(value="/datalist",method=RequestMethod.POST)
	public Map components(Map map) {
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		result.put("msg", "成功");
		result.put("data", farmInfoService.findAll(map));
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="/dataImport",method=RequestMethod.POST)
	public Map dataImport(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		return farmInfoService.dataImport(file, request);
	}
}

