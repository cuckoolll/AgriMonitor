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
import com.agri.monitor.entity.SoilInfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.CacheTypeEnum;
import com.agri.monitor.service.datamanage.SoilInfoService;
import com.agri.monitor.utils.CacheUtil;
import com.agri.monitor.vo.SoilQueryVO;

@Controller
@RequestMapping("/soilinfo")
public class SoilInfoController {
	@Autowired 
	private SoilInfoService soilInfoService;
	 
	/**
	 * 水质监测页面 .
	 * 
	 * @return .
	 */
	 @RequestMapping("") 
	 public String soilInfo() { 
		 return "/datamanage/soilinfo/soilinfo"; 
	 }
	  
	 /**
	  * 查询水质监测信息 .
	  * 
	  * @param request .
	  * @return .
	  */
	 @RequestMapping(value="/queryInfo", method = RequestMethod.POST)
	 @ResponseBody
	 @IgnoreSession 
	 public Map queryInfo(SoilQueryVO queryVo, HttpServletRequest request) {
		 UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo"); return
		 soilInfoService.queryInfoForPage(queryVo, user.getUser_id()); 
	 }

	@ResponseBody
	@RequestMapping(value="/dataImport",method=RequestMethod.POST) public Map
	dataImport(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		return soilInfoService.dataImport(file, request); 
	}

	@IgnoreSession
	@RequestMapping("/update") 
	public String add(Model model) { 
		return "/datamanage/soilinfo/soilupdate";
	}


	@ResponseBody
	@RequestMapping(value="/save",method=RequestMethod.POST) 
	public Map doUpdate(SoilInfo soilinfo, HttpServletRequest request) { 
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo"); 
		return soilInfoService.saveOrUpdate(soilinfo, user.getUser_id()); 
	}


	@ResponseBody
	@RequestMapping(value="/delInfoByGid",method=RequestMethod.POST) 
	public Map delInfoByGid(@RequestBody ArrayList<Integer> gids, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo"); return
		soilInfoService.delInfoByGid(gids, user.getUser_id()); 
	}


	@ResponseBody
	@RequestMapping(value="/findById",method=RequestMethod.POST)
	public SoilInfo findById(Integer gid, HttpServletRequest request) { 
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo"); return
		soilInfoService.findById(gid, user.getUser_id()); 
	}
	
	@RequestMapping("/soilAnalysis")
	public String soilAnalysis(Model model) {
		model.addAttribute("soilindex", CacheUtil.getCache(CacheTypeEnum.SOILINDEX));
		return "/statisticanalysis/soilanalysis/soilanalysis";
	}
	
	@ResponseBody
	@RequestMapping(value="/queryAnalysisData", method=RequestMethod.POST)
	public Map queryAnalysisData(HttpServletRequest request) {
		return soilInfoService.queryAnalysisData(request);
	}
}
