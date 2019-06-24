package com.agri.monitor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AgriMonitorController {
	@RequestMapping("/index.html")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/components.html")
	public String components() {
		return "components";
	}
	
	@RequestMapping("/forms.html")
	public String forms() {
		return "forms";
	}
	
	@RequestMapping("/icons.html")
	public String icons() {
		return "icons";
	}
	
	@RequestMapping("/notifications.html")
	public String notifications() {
		return "notifications";
	}
	
	@RequestMapping("/tables.html")
	public String tables() {
		return "tables";
	}
	
	@RequestMapping("/typography.html")
	public String typography() {
		return "typography";
	}
}
