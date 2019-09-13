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
import org.springframework.web.bind.annotation.ResponseBody;

import com.agri.monitor.annotation.IgnoreSession;
import com.agri.monitor.entity.FarmProductInfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.CacheTypeEnum;
import com.agri.monitor.service.datamanage.FarmProductInfoService;
import com.agri.monitor.utils.CacheUtil;
import com.agri.monitor.vo.FarmProductQueryVO;

@Controller
@RequestMapping("/farmproductinfo")
public class FarmProductInfoController {
	
	@Autowired
	private FarmProductInfoService farmProductInfoService;
	
	/**
	 * 渔业生产信息页面 .
	 * @return .
	 */
	@RequestMapping("")
	public String farmproductinfo(Model model) {
		model.addAttribute("towns", CacheUtil.getCache(CacheTypeEnum.TOWNS));
		return "/datamanage/farmproductinfo/farmproductinfo";
	}
	
	@RequestMapping(value="/queryInfo", method = RequestMethod.POST)
	@ResponseBody
	@IgnoreSession
	public Map queryInfo(FarmProductQueryVO queryVo, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return farmProductInfoService.queryInfoForPage(queryVo, user.getUser_id());
	}
//	
//	@ResponseBody
//	@RequestMapping(value="/dataImport",method=RequestMethod.POST)
//	public Map dataImport(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
//		return grassInfoService.dataImport(file, request);
//	}
	
	@IgnoreSession
	@RequestMapping("/update")
	public String add(Model model) {
		return "/datamanage/farmproductinfo/farmproductupdate";
	}

	@ResponseBody
	@RequestMapping(value="/save",method=RequestMethod.POST)
	public Map doUpdate(FarmProductInfo farmproductinfo, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return farmProductInfoService.saveOrUpdate(farmproductinfo, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/delInfoByGid",method=RequestMethod.POST)
	public Map delInfoByGid(@RequestBody ArrayList<Integer> gids, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return farmProductInfoService.delInfoByGid(gids, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/findById",method=RequestMethod.POST)
	public FarmProductInfo findById(Integer gid, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return farmProductInfoService.findById(gid, user.getUser_id());
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
