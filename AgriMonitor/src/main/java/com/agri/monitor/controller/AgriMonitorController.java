package com.agri.monitor.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Agri")
public class AgriMonitorController {
	@RequestMapping("/hello")
	public String hello() {
		return "hello!";
	}

}
