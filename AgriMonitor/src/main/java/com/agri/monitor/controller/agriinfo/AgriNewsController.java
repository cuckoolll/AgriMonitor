package com.agri.monitor.controller.agriinfo;

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
import com.agri.monitor.entity.AgriNewsInfo;
import com.agri.monitor.entity.SoilInfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.service.agriinfo.AgriNewsService;
import com.agri.monitor.vo.AgriNewsQueryVO;

@Controller
@RequestMapping("/agrinews")
public class AgriNewsController {
	
	@Autowired
	private AgriNewsService agriNewsService;
	
	/**
	 * 农业信息维护 .
	 * @return .
	 */
	@RequestMapping("")
	public String agrinews() {
		return "/agriinfo/agrinews/agrinews";
	}
	
	/**
	 * 查询农业信息 .
	 * @param request .
	 * @return .
	 */
	@RequestMapping(value="/queryInfo", method = RequestMethod.POST)
	@ResponseBody
	@IgnoreSession
	public Map queryInfo(AgriNewsQueryVO queryVo, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return agriNewsService.queryInfoForPage(queryVo, user.getUser_id());
	}
	
	@IgnoreSession
	@RequestMapping("/newsedit")
	public String upload(Model model) {
		return "/agriinfo/agrinews/agrinewsedit";
	}
	
	@ResponseBody
	@RequestMapping(value="/findById",method=RequestMethod.POST)
	public AgriNewsInfo findById(Integer gid, HttpServletRequest request) { 
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo"); return
		agriNewsService.findById(gid, user.getUser_id()); 
	}
	
	@ResponseBody
	@RequestMapping(value="/delInfoByGid",method=RequestMethod.POST)
	public Map delInfoByGid(@RequestBody ArrayList<Integer> gids, HttpServletRequest request) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		return agriNewsService.delInfoByGid(gids, user.getUser_id());
	}
	
	@ResponseBody
	@RequestMapping(value="/save",method=RequestMethod.POST) 
	public Map doUpdate(AgriNewsInfo agriNewsInfo, HttpServletRequest request) { 
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo"); 
		return agriNewsService.saveOrUpdate(agriNewsInfo, user.getUser_id()); 
	}
}
