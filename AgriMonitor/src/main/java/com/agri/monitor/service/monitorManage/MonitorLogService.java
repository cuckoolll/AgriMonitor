package com.agri.monitor.service.monitorManage;

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
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.vo.MonitorSetQueryVO;

@Service
public class MonitorLogService {
	
	private static final Logger logger = LoggerFactory.getLogger(MonitorLogService.class);
	
	@Autowired
	private MonitorLogMapper monitorLogMapper;
	
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
	
	public List<String> findmonitorinfo(){
		return monitorLogMapper.findmonitorinfo();
	}
}
