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
import com.agri.monitor.entity.AnimalsYearInfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.CacheTypeEnum;
import com.agri.monitor.service.datamanage.AnimalsYearInfoService;
import com.agri.monitor.utils.CacheUtil;
import com.agri.monitor.vo.AnimalsYearQueryVO;

@Controller
@RequestMapping("/animalsyearinfo")
public class AnimalsYearInfoController {
	
	@Autowired
	private AnimalsYearInfoService animalsYearInfoService;
	
	@RequestMapping("")
	public String animalsyearinfo(Model model) {
		model.addAttribute("animalsTarget", CacheUtil.getCache(CacheTypeEnum.ANIMALSTARGET));
		model.addAttribute("towns", CacheUtil.getCache(CacheTypeEnum.TOWNS));
		return "/datamanage/animalsyearinfo/animalsyearinfo";
	}
	
	@RequestMapping(value="/queryInfo", method = RequestMethod.POST)
	@ResponseBody
	@IgnoreSession
	public Map queryInfo(AnimalsYearQueryVO queryVo, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return animalsYearInfoService.queryInfoForPage(queryVo, user.getUser_id());
	}
	
	@IgnoreSession
	@RequestMapping("/update")
	public String add(Model model) {
		model.addAttribute("animalsTarget", CacheUtil.getCache(CacheTypeEnum.ANIMALSTARGET));
		return "/datamanage/animalsyearinfo/animalsyearupdate";
	}

	@ResponseBody
	@RequestMapping(value="/save",method=RequestMethod.POST)
	public Map doUpdate(AnimalsYearInfo animalsyearinfo, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return animalsYearInfoService.saveOrUpdate(animalsyearinfo, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/delInfoByGid",method=RequestMethod.POST)
	public Map delInfoByGid(@RequestBody ArrayList<Integer> gids, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return animalsYearInfoService.delInfoByGid(gids, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/findById",method=RequestMethod.POST)
	public AnimalsYearInfo findById(Integer gid, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return animalsYearInfoService.findById(gid, user.getUser_id());
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
