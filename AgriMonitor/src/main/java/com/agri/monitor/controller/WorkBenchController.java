package com.agri.monitor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WorkBenchController {
	
	@RequestMapping("/workbench")
	public String index() {
		return "/workbench";
	}
	
	@RequestMapping("/dataoverview")
	public String dataview() {
		return "/dataoverview";
	}
	
}