package com.agri.monitor.service.monitorManage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.MonitorLogMapper;
import com.agri.monitor.service.datamanage.AnimalsBreedService;
import com.agri.monitor.service.datamanage.CropsPlantService;
import com.agri.monitor.service.datamanage.FarmInfoService;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.vo.MonitorSetQueryVO;

@Service
public class MonitorLogService {
	
	private static final Logger logger = LoggerFactory.getLogger(MonitorLogService.class);
	
	@Autowired
	private MonitorLogMapper monitorLogMapper;
	@Autowired
	private MonitorSetService monitorSetService;
	@Autowired
	private FarmInfoService farmInfoService;
	@Autowired
	private CropsPlantService cropsPlantService;
	@Autowired
	private AnimalsBreedService animalsBreedService;
	
	public Map doDel(List<Integer> gids, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("查询数据监控记录信息删除开始：" + gids);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			monitorLogMapper.delete(gids);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "删除数据监控记录信息，GID="+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除数据监控记录信息异常" + e);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "删除数据监控记录信息异常："+e.getMessage());
		}
		return result;
	}
	
	public List<Map> findAllForPage(MonitorSetQueryVO queryVO, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("查询所有数据监控记录信息开始：" + queryVO);
		}
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询数据监控记录信息，"+queryVO);
		return monitorLogMapper.findAllForPage(queryVO);
	}
	
	public int findAllCount(MonitorSetQueryVO queryVO) {
		return monitorLogMapper.findAllCount(queryVO);
	}
	
	public Map updatestatus(Integer stopflag,Integer gid, String userid) {
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		Map map = new HashMap<>();
		map.put("stopflag", stopflag);
		map.put("gid", gid);
		monitorLogMapper.updateStatus(map);
		LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "监控数据状态更新成功stopflag="+stopflag+",gid="+gid);
		return result;
	}
	
	public List<Map> findmonitorinfo(){
		return monitorLogMapper.findmonitorinfo();
	}
	//数据监控
    public void dataMonitorTasks() {
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
	//查询设置的监控数据
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
	
	//删除一月监控数据
    public void deleteMonitorlog() {
		monitorLogMapper.deleteAll();
		dataMonitorTasks();
	}
    
	private void info(String msg) {
		if (logger.isInfoEnabled()) {
			logger.info(msg);
		}
	}
}
