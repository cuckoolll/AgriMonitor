package com.agri.monitor.controller.monitorSetManage;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.agri.monitor.annotation.IgnoreSession;
import com.agri.monitor.entity.MonitorSet;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.CacheTypeEnum;
import com.agri.monitor.service.monitorManage.MonitorLogService;
import com.agri.monitor.service.monitorManage.MonitorSetService;
import com.agri.monitor.utils.CacheUtil;
import com.agri.monitor.vo.MonitorSetQueryVO;

@Controller
@RequestMapping("/monitorManage")
public class MonitorManageController {
	
	@Autowired
	private MonitorSetService monitorSetService;
	@Autowired
	private MonitorLogService monitorLogService;
	
	@RequestMapping("")
	public String toPage(Model model) {
		return "/monitorManage/setlist";
	}
	
	@IgnoreSession
	@RequestMapping("update")
	public String add(Model model) {
		return "/monitorManage/update";
	}
	
	@ResponseBody
	@RequestMapping(value="/doDel",method=RequestMethod.POST)
	public Map doDel(@RequestBody ArrayList<Integer> gids, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return monitorSetService.doDel(gids, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/doSave",method=RequestMethod.POST)
	public Map doUpdate(MonitorSet monitorSet,HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return monitorSetService.saveOrUpdate(monitorSet,user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/findById",method=RequestMethod.POST)
	public MonitorSet findById(Integer gid, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return monitorSetService.findById(gid, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/setlist",method=RequestMethod.POST)
	public Map setlist(MonitorSetQueryVO queryVO, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		result.put("msg", "成功");
		result.put("data", monitorSetService.findAllForPage(queryVO, user.getUser_id()));
		result.put("count", monitorSetService.findAllCount(queryVO));
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="/findTargetType",method=RequestMethod.POST)
	public Map findTargetType() {
		List<Map> types1 = new ArrayList<>();
		List<Map> list = (List<Map>) CacheUtil.getCache(CacheTypeEnum.CROPSTYPE);
		if(null != list) {
			for (Map map : list) {
				if((Integer)map.get("stopflag")==1) {
					types1.add(map);
				}
			}
		}
		List<Map> types2 = new ArrayList<>();
		List<Map> list2 = (List<Map>) CacheUtil.getCache(CacheTypeEnum.ANIMALSTYPE);
		if(null != list2) {
			for (Map map : list2) {
				if((Integer)map.get("stopflag")==1) {
					types2.add(map);
				}
			}
		}
		List<Map> types3 = new ArrayList<>();
		List<Map> list3 = (List<Map>) CacheUtil.getCache(CacheTypeEnum.ANIMALSTARGET);
		if(null != list3) {
			for (Map map : list3) {
				if((Integer)map.get("stopflag")==1) {
					types3.add(map);
				}
			}
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("cropstype", types1);
		result.put("animalstype", types2);
		result.put("animalstarget", types3);
		return result;
	}
	/**------------监控记录---------------**/
	@RequestMapping("monitorlog")
	public String monitorlog(Model model) {
		return "/monitorManage/monitorlog";
	}
	@ResponseBody
	@RequestMapping("datamonitor")
	public String datamonitor(Model model) {
		monitorLogService.dataMonitorTasks();
		return "true";
	}
	@ResponseBody
	@RequestMapping(value="/loglist",method=RequestMethod.POST)
	public Map loglist(MonitorSetQueryVO queryVO, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		result.put("msg", "成功");
		result.put("data", monitorLogService.findAllForPage(queryVO, user.getUser_id()));
		result.put("count", monitorLogService.findAllCount(queryVO));
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="/delLog",method=RequestMethod.POST)
	public Map delLog(@RequestBody ArrayList<Integer> gids, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return monitorLogService.doDel(gids, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/updatestatus",method=RequestMethod.POST)
	public Map updatestatus(Integer stopflag,Integer gid, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return monitorLogService.updatestatus(stopflag, gid, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/findmonitorinfo",method=RequestMethod.POST)
	public List<Map> findmonitorinfo() {
		return monitorLogService.findmonitorinfo();
	}
}

