package com.agri.monitor.controller.datamanage;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.agri.monitor.annotation.IgnoreSession;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.CacheTypeEnum;
import com.agri.monitor.utils.CacheUtil;
import com.agri.monitor.vo.GrassQueryVO;

@Controller
@RequestMapping("/farmerlifeinfo")
public class FarmerLifeInfoController {
	
//	@Autowired
//	private FishInfoService fishInfoService;
	
	@RequestMapping("")
	public String farmerLifeInfo(Model model) {
		model.addAttribute("towns", CacheUtil.getCache(CacheTypeEnum.TOWNS));
		return "/datamanage/farmerlifeinfo/farmerlifeinfo";
	}
	
	@RequestMapping(value="/queryInfo", method = RequestMethod.POST)
	@ResponseBody
	@IgnoreSession
	public Map queryInfo(GrassQueryVO queryVo, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
//		return grassInfoService.queryInfoForPage(queryVo, user.getUser_id());
		Map map = new HashMap();
		map.put("code", 0);
		return map;
	}
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
