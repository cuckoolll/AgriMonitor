package com.agri.monitor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@MapperScan("com.agri.monitor.mapper")
@SpringBootApplication
public class AgriMonitorApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(AgriMonitorApplication.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(AgriMonitorApplication.class, args);
	}

}
