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
import com.agri.monitor.service.monitorManage.MonitorSetService;
import com.agri.monitor.vo.MonitorSetQueryVO;

@Configuration
@EnableScheduling
public class JobConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(JobConfig.class);
	
	@Autowired
	private MonitorSetService monitorSetService;
	@Autowired
	private FarmInfoService farmInfoService;
	@Autowired
	private CropsPlantService cropsPlantService;
	@Autowired
	private AnimalsBreedService animalsBreedService;
	@Autowired
	private MonitorLogMapper monitorLogMapper;
	@Autowired
	private SystemLogMapper systemLogMapper;
	
	//每周一1点运行该调度(清理一周的日志)
	@Scheduled(cron = "0 0 01 ? * 1")
	//@Scheduled(cron = "0/40 * * * * ?")
    private void sysLogTasks() {
		info("清理一周的日志开始");
		systemLogMapper.deleteAll();
		info("清理一周的日志完成");
	}
	
	//每年的1月1号1点，删除全部预警信息
	@Scheduled(cron = "0 0 01 1 1 ?")
	//@Scheduled(cron = "0/50 * * * * ?")
    private void deleteMonitorlog() {
		monitorLogMapper.deleteAll();
		dataMonitorTasks();
	}
	
	//每周一2点运行该调度(数据监控)
	@Scheduled(cron = "0 0 02 ? * 1")
	//@Scheduled(cron = "0/10 * * * * ?")
    private void dataMonitorTasks() {
		info("运行数据监控调度开始");
		//将之前预警信息设置为停用状态
		monitorLogMapper.updateStatusAll();
		//查询为停用的设置的数据
		MonitorSetQueryVO queryvo = new MonitorSetQueryVO();
		queryvo.setPage(1);
		queryvo.setLimit(Integer.MAX_VALUE);
		queryvo.setStopflag(1);
		List<Map> list = monitorSetService.findAllForPage(queryvo, "系统调度");
		if (null != list && list.size() > 0) {
			info("查询到配置的数据监控设置数据，size="+list.size());
			Map<String, List<Map>> monitorSetGroup = monitorSetGroup(list);
			//处理养殖场数据监控
			farmInfoService.dataMonitorHandle(monitorSetGroup.get("1"), monitorSetGroup.get("2"));
			//畜牧业生产情况预警监控
			animalsBreedService.dataMonitorHandle(monitorSetGroup.get("3"));
			//农作物产量预警监控
			cropsPlantService.dataMonitorHandle(monitorSetGroup.get("4"));
		}
		info("运行数据监控调度结束");
    }
	
	private Map<String, List<Map>> monitorSetGroup(List<Map> list) {
		Map<String, List<Map>> monitorSetGroup = new HashMap<>();
		//按监控类型分组
		for (Map map : list) {
			List<Map> temlist = monitorSetGroup.get(map.get("monitor_type").toString());
			if (temlist == null) {
				temlist = new ArrayList<>();
				temlist.add(map);
				monitorSetGroup.put(map.get("monitor_type").toString(), temlist);
			}else {
				temlist.add(map);
			}
		}
		return monitorSetGroup;
	}
	
	private void info(String msg) {
		if (logger.isInfoEnabled()) {
			logger.info(msg);
		}
	}
}
