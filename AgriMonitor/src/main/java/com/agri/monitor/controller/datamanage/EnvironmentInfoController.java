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
import com.agri.monitor.entity.EnvironmentInfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.CacheTypeEnum;
import com.agri.monitor.service.datamanage.EnvironmentInfoService;
import com.agri.monitor.utils.CacheUtil;
import com.agri.monitor.vo.EnvironmentQueryVO;

@Controller
@RequestMapping("/environmentinfo")
public class EnvironmentInfoController {
	
	@Autowired
	private EnvironmentInfoService environmentInfoService;
	
	@RequestMapping("")
	public String environmentinfo(Model model) {
		model.addAttribute("towns", CacheUtil.getCache(CacheTypeEnum.TOWNS));
		return "/datamanage/environmentinfo/environmentinfo";
	}
	
	/**
	 * 查询草原生态监测信息 .
	 * @param request .
	 * @return .
	 */
	@RequestMapping(value="/queryInfo", method = RequestMethod.POST)
	@ResponseBody
	@IgnoreSession
	public Map queryInfo(EnvironmentQueryVO queryVo, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return environmentInfoService.queryInfoForPage(queryVo, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/dataImport",method=RequestMethod.POST)
	public Map dataImport(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		return environmentInfoService.dataImport(file, request);
	}
	
	@IgnoreSession
	@RequestMapping("/update")
	public String add(Model model) {
		return "/datamanage/environmentinfo/environmentupdate";
	}

	@ResponseBody
	@RequestMapping(value="/save",method=RequestMethod.POST)
	public Map doUpdate(EnvironmentInfo environmentinfo, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return environmentInfoService.saveOrUpdate(environmentinfo, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/delInfoByGid",method=RequestMethod.POST)
	public Map delInfoByGid(@RequestBody ArrayList<Integer> gids, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return environmentInfoService.delInfoByGid(gids, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/findById",method=RequestMethod.POST)
	public EnvironmentInfo findById(Integer gid, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return environmentInfoService.findById(gid, user.getUser_id());
	}
	
//	@RequestMapping("/grassAnalysis")
//	public String grassAnalysis(Model model) {
//		model.addAttribute("grassindex", CacheUtil.getCache(CacheTypeEnum.GRASSINDEX));
//		model.addAttribute("towns", CacheUtil.getCache(CacheTypeEnum.TOWNS));
//		return "/statisticanalysis/grassanalysis/grassanalysis";
//	}
//	
//	@ResponseBody
//	@RequestMapping(value="/queryAnalysisData", method=RequestMethod.POST)
//	public Map queryAnalysisData(HttpServletRequest request) {
//		return grassInfoService.queryAnalysisData(request);
//	}
}
