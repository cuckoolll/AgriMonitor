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
import com.agri.monitor.vo.EnvironmentQueryVO;

@Controller
@RequestMapping("/facilitiesconditioninfo")
public class FacilitiesConditionInfoController {
	
//	@Autowired
//	private EnvironmentInfoService environmentInfoService;
	
	/**
	 * 草原生态监测页面 .
	 * @return .
	 */
	@RequestMapping("")
	public String proTeamAndEmployInfo(Model model) {
		model.addAttribute("towns", CacheUtil.getCache(CacheTypeEnum.TOWNS));
		return "/datamanage/facilitiesconditioninfo/facilitiesconditioninfo";
	}
	
	@RequestMapping(value="/queryInfo", method = RequestMethod.POST)
	@ResponseBody
	@IgnoreSession
	public Map queryInfo(EnvironmentQueryVO queryVo, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
//		return environmentInfoService.queryInfoForPage(queryVo, user.getUser_id());
		final Map param = new HashMap();
		param.put("code", 0);
		param.put("msg", "成功");
		param.put("count", 0);
		param.put("data", new HashMap());
		return param;
	}
	
//	@ResponseBody
//	@RequestMapping(value="/dataImport",method=RequestMethod.POST)
//	public Map dataImport(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
//		return environmentInfoService.dataImport(file, request);
//	}
	
//	@IgnoreSession
//	@RequestMapping("/update")
//	public String add(Model model) {
//		return "/datamanage/environmentinfo/environmentupdate";
//	}

//	@ResponseBody
//	@RequestMapping(value="/save",method=RequestMethod.POST)
//	public Map doUpdate(EnvironmentInfo environmentinfo, HttpServletRequest request) {
//		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
//		return environmentInfoService.saveOrUpdate(environmentinfo, user.getUser_id());
//	}
	
//	@ResponseBody
//	@RequestMapping(value="/delInfoByGid",method=RequestMethod.POST)
//	public Map delInfoByGid(@RequestBody ArrayList<Integer> gids, HttpServletRequest request) {
//		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
//		return environmentInfoService.delInfoByGid(gids, user.getUser_id());
//	}
	
//	@ResponseBody
//	@RequestMapping(value="/findById",method=RequestMethod.POST)
//	public EnvironmentInfo findById(Integer gid, HttpServletRequest request) {
//		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
//		return environmentInfoService.findById(gid, user.getUser_id());
//	}
	
}
