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
import com.agri.monitor.entity.GrassInfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.CacheTypeEnum;
import com.agri.monitor.service.datamanage.GrassInfoService;
import com.agri.monitor.utils.CacheUtil;
import com.agri.monitor.vo.GrassQueryVO;

@Controller
@RequestMapping("/farmproductinfo")
public class FarmProductInfoController {
	
//	@Autowired
//	private FishInfoService fishInfoService;
	
	/**
	 * 渔业生产信息页面 .
	 * @return .
	 */
	@RequestMapping("")
	public String farmproductinfo(Model model) {
		model.addAttribute("towns", CacheUtil.getCache(CacheTypeEnum.TOWNS));
		return "/datamanage/farmproductinfo/farmproductinfo";
	}
	
//	/**
//	 * 查询草原生态监测信息 .
//	 * @param request .
//	 * @return .
//	 */
//	@RequestMapping(value="/queryInfo", method = RequestMethod.POST)
//	@ResponseBody
//	@IgnoreSession
//	public Map queryInfo(GrassQueryVO queryVo, HttpServletRequest request) {
//		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
//		return grassInfoService.queryInfoForPage(queryVo, user.getUser_id());
//	}
//	
//	@ResponseBody
//	@RequestMapping(value="/dataImport",method=RequestMethod.POST)
//	public Map dataImport(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
//		return grassInfoService.dataImport(file, request);
//	}
//	
//	@IgnoreSession
//	@RequestMapping("/update")
//	public String add(Model model) {
//		return "/datamanage/grassinfo/grassupdate";
//	}
//
//	@ResponseBody
//	@RequestMapping(value="/save",method=RequestMethod.POST)
//	public Map doUpdate(GrassInfo grassinfo, HttpServletRequest request) {
//		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
//		return grassInfoService.saveOrUpdate(grassinfo, user.getUser_id());
//	}
//	
//	@ResponseBody
//	@RequestMapping(value="/delInfoByGid",method=RequestMethod.POST)
//	public Map delInfoByGid(@RequestBody ArrayList<Integer> gids, HttpServletRequest request) {
//		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
//		return grassInfoService.delInfoByGid(gids, user.getUser_id());
//	}
//	
//	@ResponseBody
//	@RequestMapping(value="/findById",method=RequestMethod.POST)
//	public GrassInfo findById(Integer gid, HttpServletRequest request) {
//		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
//		return grassInfoService.findById(gid, user.getUser_id());
//	}
//	
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
