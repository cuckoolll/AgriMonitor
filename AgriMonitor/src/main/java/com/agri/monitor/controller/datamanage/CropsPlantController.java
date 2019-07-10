package com.agri.monitor.controller.datamanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.agri.monitor.entity.CropsPlant;
import com.agri.monitor.entity.CropsType;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.CacheTypeEnum;
import com.agri.monitor.service.datamanage.CropsPlantService;
import com.agri.monitor.service.datamanage.CropsTypeService;
import com.agri.monitor.utils.CacheUtil;
import com.agri.monitor.vo.CropsPlantQueryVO;
import com.agri.monitor.vo.CropsTypeQueryVO;

@Controller
@RequestMapping("/cropsplant")
public class CropsPlantController {
	
	@Autowired
	private CropsPlantService cropsPlantService;
	
	@Autowired
	private CropsTypeService cropsTypeService;
	
	@RequestMapping("")
	public String toPage(Model model) {
		model.addAttribute("cropstype", CacheUtil.getCache(CacheTypeEnum.CROPSTYPE));
		model.addAttribute("towns", CacheUtil.getCache(CacheTypeEnum.TOWNS));
		return "/datamanage/cropsplant/datalist";
	}
	
	@IgnoreSession
	@RequestMapping("update")
	public String add(Model model) {
		model.addAttribute("towns", CacheUtil.getCache(CacheTypeEnum.TOWNS));
		model.addAttribute("cropstype", CacheUtil.getCache(CacheTypeEnum.CROPSTYPE));
		return "/datamanage/cropsplant/update";
	}
	
	@ResponseBody
	@RequestMapping(value="/doDel",method=RequestMethod.POST)
	public Map doDel(@RequestBody ArrayList<Integer> gids, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return cropsPlantService.doDel(gids, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/doSave",method=RequestMethod.POST)
	public Map doUpdate(CropsPlant cropsPlant,HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return cropsPlantService.saveOrUpdate(cropsPlant,user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/findById",method=RequestMethod.POST)
	public CropsPlant findById(Integer gid, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return cropsPlantService.findById(gid, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/datalist",method=RequestMethod.POST)
	public Map datalist(CropsPlantQueryVO queryVO, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		result.put("msg", "成功");
		result.put("data", cropsPlantService.findAllForPage(queryVO, user.getUser_id()));
		result.put("count", cropsPlantService.findAllCount(queryVO));
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="/dataImport",method=RequestMethod.POST)
	public Map dataImport(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		return cropsPlantService.dataImport(file, request);
	}
	
	@RequestMapping("/download")
    public void downloadFile(HttpServletResponse response) {
		cropsPlantService.downloadFile(response);
	}
	
	@RequestMapping("/cropstype")
	public String toTypePage(Model model) {
		return "/datamanage/cropsplant/cropstype/datalist";
	}
	
	@IgnoreSession
	@RequestMapping("/cropstype/update")
	public String addAnimalsType(Model model) {
		return "/datamanage/cropsplant/cropstype/update";
	}
	
	@ResponseBody
	@RequestMapping(value="/cropstype/doDel",method=RequestMethod.POST)
	public Map delType(@RequestBody ArrayList<Integer> gids, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return cropsTypeService.doDel(gids, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/cropstype/doSave",method=RequestMethod.POST)
	public Map updateType(CropsType cropsType,HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return cropsTypeService.saveOrUpdate(cropsType,user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/cropstype/findById",method=RequestMethod.POST)
	public CropsType findTypeById(Integer gid, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return cropsTypeService.findById(gid, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/cropstype/datalist",method=RequestMethod.POST)
	public Map cropsTypeList(CropsTypeQueryVO queryVO, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		result.put("msg", "成功");
		result.put("data", cropsTypeService.findAllForPage(queryVO, user.getUser_id()));
		result.put("count", cropsTypeService.findAllCount(queryVO));
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="/cropstype/qy",method=RequestMethod.POST)
	public Map cropstypeQy(@RequestBody ArrayList<Integer> gids,HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return cropsTypeService.cropstypeQy(gids,user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/cropstype/ty",method=RequestMethod.POST)
	public Map cropstypeTy(@RequestBody ArrayList<Integer> gids,HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return cropsTypeService.cropstypeTy(gids,user.getUser_id());
	}
	
}

