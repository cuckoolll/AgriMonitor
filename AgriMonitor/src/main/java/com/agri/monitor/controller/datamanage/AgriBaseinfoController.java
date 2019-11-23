package com.agri.monitor.controller.datamanage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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
import com.agri.monitor.entity.AgriBaseinfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.CacheTypeEnum;
import com.agri.monitor.service.datamanage.AgriBaseinfoService;
import com.agri.monitor.service.datamanage.AreainfoService;
import com.agri.monitor.utils.CacheUtil;
import com.agri.monitor.vo.AgriBaseinfoQueryVO;

@Controller
@RequestMapping("/agriBaseinfo")
public class AgriBaseinfoController {
	
	@Autowired
	private AgriBaseinfoService agriBaseinfoService;
	
	@Autowired
	private AreainfoService areainfoService;
	
	@RequestMapping("")
	public String toPage(Model model) {
		model.addAttribute("towns", CacheUtil.getCache(CacheTypeEnum.TOWNS));
		return "/datamanage/agriculture/datalist";
	}
	
	@IgnoreSession
	@RequestMapping("update")
	public String add(Model model) {
		model.addAttribute("towns", CacheUtil.getCache(CacheTypeEnum.TOWNS));
		return "/datamanage/agriculture/update";
	}
	
	@ResponseBody
	@RequestMapping(value="/doDel",method=RequestMethod.POST)
	public Map doDel(@RequestBody ArrayList<Integer> gids, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return agriBaseinfoService.doDel(gids, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/doSave",method=RequestMethod.POST)
	public Map doUpdate(AgriBaseinfo info,HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return agriBaseinfoService.saveOrUpdate(info,user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/findById",method=RequestMethod.POST)
	public AgriBaseinfo findById(Integer gid, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return agriBaseinfoService.findById(gid, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/datalist",method=RequestMethod.POST)
	public Map datalist(AgriBaseinfoQueryVO queryVO, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		result.put("msg", "成功");
		result.put("data", agriBaseinfoService.findAllForPage(queryVO, user.getUser_id()));
		result.put("count", agriBaseinfoService.findAllCount(queryVO));
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="/dataImport",method=RequestMethod.POST)
	public Map dataImport(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		return agriBaseinfoService.dataImport(file, request);
	}
	
	@ResponseBody
	@RequestMapping(value="/find4Maps",method=RequestMethod.POST)
	public Map find4Maps(HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		
		final Map<String, Object> result = new HashMap<String, Object>();
		
		Calendar c = Calendar.getInstance();
		AgriBaseinfoQueryVO queryVO = new AgriBaseinfoQueryVO();
		queryVO.setPage(1);
		queryVO.setLimit(500);
		queryVO.setDate_year(c.get(Calendar.YEAR));
		queryVO.setDate_year1(c.get(Calendar.YEAR));
		List<Map> list = agriBaseinfoService.findAllForPage(queryVO, user.getUser_id());
		List<Map> temlist = new ArrayList<>();
		if(null != list && list.size() > 0) {
			//地图数据
			for (Map map : list) {
				Map m = new HashMap<>();
				if(null == map.get("village")) {
					continue;
				}
				String str = map.get("village").toString().replace("村", "");
				m.put("name", str);
				m.put("size", 40);
				m.put("data", map);
				temlist.add(m);
			}
		}
		result.put("data", temlist);
		result.put("areainfo", areainfoService.findAll(user.getUser_id()));
		return result;
	}
}

