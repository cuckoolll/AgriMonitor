package com.agri.monitor.controller.agriinfo;

import java.util.ArrayList;
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
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.service.agriinfo.PolicyMaintainService;
import com.agri.monitor.vo.PolicyQueryVO;

@Controller
@RequestMapping("/agrinews")
public class AgriInfoController {
	
	@Autowired
	private PolicyMaintainService policyMaintainService;
	
	/**
	 * 农业信息维护 .
	 * @return .
	 */
	@RequestMapping("")
	public String agrinews() {
		return "/agriinfo/agrinews/agrinews";
	}
	
	/**
	 * 查询农业政策文件 .
	 * @param request .
	 * @return .
	 */
	@RequestMapping(value="/queryInfo", method = RequestMethod.POST)
	@ResponseBody
	@IgnoreSession
	public Map queryInfo(PolicyQueryVO queryVo, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return policyMaintainService.queryInfoForPage(queryVo, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/fileUpload",method=RequestMethod.POST)
	public Map fileUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
		return policyMaintainService.fileUpload(file, request);
	}
	
	@IgnoreSession
	@RequestMapping("/upload")
	public String upload(Model model) {
		return "/agriinfo/policymaintain/policyupload";
	}
	
	@RequestMapping(value="/download",method=RequestMethod.POST)
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) {
		policyMaintainService.downloadFile(request, response);
	}
	
	@ResponseBody
	@RequestMapping(value="/delInfoByGid",method=RequestMethod.POST)
	public Map delInfoByGid(@RequestBody ArrayList<Integer> gids, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return policyMaintainService.delInfoByGid(gids, user.getUser_id());
	}
	
}
