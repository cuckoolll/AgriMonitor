package com.agri.monitor.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.agri.monitor.mapper.MonitorLogMapper;
import com.agri.monitor.mapper.SystemLogMapper;
import com.agri.monitor.service.datamanage.AnimalsBreedService;
import com.agri.monitor.service.datamanage.CropsPlantService;
import com.agri.monitor.service.datamanage.FarmInfoService;
import com.agri.monitor.service.monitorManage.MonitorLogService;
import com.agri.monitor.service.monitorManage.MonitorSetService;
import com.agri.monitor.vo.MonitorSetQueryVO;

@Configuration
@EnableScheduling
public class JobConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(JobConfig.class);
	
	@Autowired
	private MonitorLogService monitorLogService;
	
	@Autowired
	private SystemLogMapper systemLogMapper;
	
	//每周一1点运行该调度(清理一周的日志)
	@Scheduled(cron = "0 0 01 ? * 1")
//	@Scheduled(cron = "0/40 * * * * ?")
    private void sysLogTasks() {
		info("清理一周的日志开始");
		systemLogMapper.deleteAll();
		info("清理一周的日志完成");
	}
	
	//每年的1月1号1点，删除全部预警信息
	@Scheduled(cron = "0 0 01 1 1 ?")
	//@Scheduled(cron = "0/50 * * * * ?")
    private void deleteMonitorlog() {
		monitorLogService.deleteMonitorlog();
	}
	
	//每周一2点运行该调度(数据监控)
	@Scheduled(cron = "0 0 02 ? * 1")
//	@Scheduled(cron = "0/120 * * * * ?")
    private void dataMonitorTasks() {
		monitorLogService.dataMonitorTasks();
	}
	
	private void info(String msg) {
		if (logger.isInfoEnabled()) {
			logger.info(msg);
		}
	}
}
