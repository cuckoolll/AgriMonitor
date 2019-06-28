package com.agri.monitor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WorkBenchController {
	
	@RequestMapping("/workbench.html")
	public String index() {
		return "workbench";
	}
	
	@RequestMapping("/components.html")
	public String components() {
		return "components";
	}
	
	@RequestMapping("/dataview.html")
	public String dataview() {
		return "/dataview/dataview";
	}
}