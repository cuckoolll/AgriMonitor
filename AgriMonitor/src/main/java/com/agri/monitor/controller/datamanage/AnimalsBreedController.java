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
import com.agri.monitor.entity.AnimalsBreed;
import com.agri.monitor.entity.AnimalsTarget;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.CacheTypeEnum;
import com.agri.monitor.service.datamanage.AnimalsBreedService;
import com.agri.monitor.service.datamanage.AnimalsTargetService;
import com.agri.monitor.utils.CacheUtil;
import com.agri.monitor.vo.AnimalsBreedQueryVO;
import com.agri.monitor.vo.AnimalsTargetQueryVO;

@Controller
@RequestMapping("/animalsBreed")
public class AnimalsBreedController {
	
	@Autowired
	private AnimalsBreedService animalsBreedService;
	
	@Autowired
	private AnimalsTargetService animalsTargetService;
	
	@RequestMapping("")
	public String toPage(Model model) {
		model.addAttribute("animalsTarget", CacheUtil.getCache(CacheTypeEnum.ANIMALSTARGET));
		return "/datamanage/animalsBreed/datalist";
	}
	
	@IgnoreSession
	@RequestMapping("update")
	public String add(Model model) {
		model.addAttribute("animalstype", CacheUtil.getCache(CacheTypeEnum.ANIMALSTYPE));
		return "/datamanage/animalsBreed/update";
	}
	
	@ResponseBody
	@RequestMapping(value="/doDel",method=RequestMethod.POST)
	public Map doDel(@RequestBody ArrayList<Integer> gids, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return animalsBreedService.doDel(gids, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/doSave",method=RequestMethod.POST)
	public Map doUpdate(AnimalsBreed animalsBreed,HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return animalsBreedService.saveOrUpdate(animalsBreed,user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/findById",method=RequestMethod.POST)
	public AnimalsBreed findById(Integer gid, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return animalsBreedService.findById(gid, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/datalist",method=RequestMethod.POST)
	public Map datalist(AnimalsBreedQueryVO queryVO, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		result.put("msg", "成功");
		result.put("data", animalsBreedService.findAllForPage(queryVO, user.getUser_id()));
		result.put("count", animalsBreedService.findAllCount(queryVO));
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="/dataImport",method=RequestMethod.POST)
	public Map dataImport(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		return animalsBreedService.dataImport(file, request);
	}
	
	/******************认定畜种维护*****************/
	@RequestMapping("/animalsTarget")
	public String toAnimalsTypePage(Model model) {
		return "/datamanage/animalsBreed/animalsTarget/datalist";
	}
	
	@IgnoreSession
	@RequestMapping("/animalsTarget/update")
	public String addAnimalsType(Model model) {
		model.addAttribute("animalstarget", CacheUtil.getCache(CacheTypeEnum.ANIMALSTARGET));
		return "/datamanage/animalsBreed/animalsTarget/update";
	}
	
	@ResponseBody
	@RequestMapping(value="/animalsTarget/doDel",method=RequestMethod.POST)
	public Map delAnimalsType(@RequestBody ArrayList<Integer> gids, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return animalsTargetService.doDel(gids, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/animalsTarget/doSave",method=RequestMethod.POST)
	public Map updateAnimalsType(AnimalsTarget animalsTarget,HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return animalsTargetService.saveOrUpdate(animalsTarget,user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/animalsTarget/findById",method=RequestMethod.POST)
	public AnimalsTarget findAnimalsTargetById(Integer gid, Model model, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		model.addAttribute("animalstarget", CacheUtil.getCache(CacheTypeEnum.ANIMALSTARGET));
		return animalsTargetService.findById(gid, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/animalsTarget/datalist")
	public Map animalsTypeList(AnimalsTargetQueryVO queryVO, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		result.put("msg", "成功");
		result.put("data", animalsTargetService.findAllForPage(queryVO, user.getUser_id()));
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="/animalsTarget/qy",method=RequestMethod.POST)
	public Map animalstypeQy(@RequestBody ArrayList<Integer> gids,HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return animalsTargetService.animalsTargetQy(gids,user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/animalsTarget/ty",method=RequestMethod.POST)
	public Map animalstypeTy(@RequestBody ArrayList<Integer> gids,HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return animalsTargetService.animalsTargetTy(gids,user.getUser_id());
	}
	
}

