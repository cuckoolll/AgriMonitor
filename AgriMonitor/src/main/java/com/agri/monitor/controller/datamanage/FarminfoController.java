package com.agri.monitor.controller.datamanage;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.agri.monitor.entity.AnimalsType;
import com.agri.monitor.entity.FarmInfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.CacheTypeEnum;
import com.agri.monitor.service.datamanage.AnimalsTypeService;
import com.agri.monitor.service.datamanage.FarmInfoService;
import com.agri.monitor.utils.CacheUtil;
import com.agri.monitor.vo.AnimalsTypeQueryVO;
import com.agri.monitor.vo.FarmQueryVO;

@Controller
@RequestMapping("/farminfo")
public class FarminfoController {
	
	@Autowired
	private FarmInfoService farmInfoService;
	
	@Autowired
	private AnimalsTypeService animalsTypeService;
	
	@RequestMapping("")
	public String toPage(Model model) {
		model.addAttribute("animalstype", CacheUtil.getCache(CacheTypeEnum.ANIMALSTYPE));
		return "/datamanage/farminfo/datalist";
	}
	
	@IgnoreSession
	@RequestMapping("update")
	public String add(Model model) {
		model.addAttribute("animalstype", CacheUtil.getCache(CacheTypeEnum.ANIMALSTYPE));
		return "/datamanage/farminfo/update";
	}
	
	@ResponseBody
	@RequestMapping(value="/doDel",method=RequestMethod.POST)
	public Map doDel(@RequestBody ArrayList<Integer> gids, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return farmInfoService.doDel(gids, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/doSave",method=RequestMethod.POST)
	public Map doUpdate(FarmInfo farminfo,HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return farmInfoService.saveOrUpdate(farminfo,user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/findById",method=RequestMethod.POST)
	public FarmInfo findById(Integer gid, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return farmInfoService.findById(gid, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/datalist",method=RequestMethod.POST)
	public Map datalist(FarmQueryVO farmQueryVO, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		result.put("msg", "成功");
		result.put("data", farmInfoService.findAllForPage(farmQueryVO, user.getUser_id()));
		result.put("count", farmInfoService.findAllCount(farmQueryVO));
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="/dataImport",method=RequestMethod.POST)
	public Map dataImport(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		return farmInfoService.dataImport(file, request);
	}
	
	/******************认定畜种维护*****************/
	@RequestMapping("/animalstype")
	public String toAnimalsTypePage(Model model) {
		return "/datamanage/farminfo/animalstype/datalist";
	}
	
	@IgnoreSession
	@RequestMapping("/animalstype/update")
	public String addAnimalsType(Model model) {
		model.addAttribute("animalstype", CacheUtil.getCache(CacheTypeEnum.ANIMALSTYPE));
		return "/datamanage/farminfo/animalstype/update";
	}
	
	@ResponseBody
	@RequestMapping(value="/animalstype/doDel",method=RequestMethod.POST)
	public Map delAnimalsType(@RequestBody ArrayList<Integer> gids, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return animalsTypeService.doDel(gids, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/animalstype/doSave",method=RequestMethod.POST)
	public Map updateAnimalsType(AnimalsType animalsType,HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return animalsTypeService.saveOrUpdate(animalsType,user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/animalstype/findById",method=RequestMethod.POST)
	public AnimalsType findAnimalsTypeById(Integer gid, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return animalsTypeService.findById(gid, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/animalstype/datalist",method=RequestMethod.POST)
	public Map animalsTypeList(AnimalsTypeQueryVO queryVO, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		result.put("msg", "成功");
		result.put("data", animalsTypeService.findAllForPage(queryVO, user.getUser_id()));
		result.put("count", animalsTypeService.findAllCount(queryVO));
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="/animalstype/qy",method=RequestMethod.POST)
	public Map animalstypeQy(@RequestBody ArrayList<Integer> gids,HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return animalsTypeService.animalstypeQy(gids,user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/animalstype/ty",method=RequestMethod.POST)
	public Map animalstypeTy(@RequestBody ArrayList<Integer> gids,HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return animalsTypeService.animalstypeTy(gids,user.getUser_id());
	}
	
}

