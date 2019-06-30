package com.agri.monitor.controller.datamanage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/farminfo")
public class FarminfoController {
	
	@RequestMapping("/datalist")
	public String components() {
		return "/datamanage/farminfo/datalist";
	}
}

