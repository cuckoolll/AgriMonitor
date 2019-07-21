package com.agri.monitor.service.monitorManage;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.agri.monitor.entity.MonitorSet;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.MonitorLogMapper;
import com.agri.monitor.mapper.MonitorSetMapper;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.vo.MonitorSetQueryVO;

@Service
public class MonitorSetService {
	
	private static final Logger logger = LoggerFactory.getLogger(MonitorSetService.class);
	
	@Autowired
	private MonitorSetMapper monitorSetMapper;
	@Autowired
	private MonitorLogMapper monitorLogMapper;
	
	public MonitorSet findById(Integer gid, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("查询数据监控设置信息，GID=" + gid);
		}
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询数据监控设置信息，GID="+gid);
		return monitorSetMapper.findById(gid);
	}
	
	public Map doDel(List<Integer> gids, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("查询数据监控设置信息删除开始：" + gids);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			monitorSetMapper.delete(gids);
			monitorLogMapper.deleteByType(gids);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "删除数据监控设置信息，GID="+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除数据监控设置信息异常" + e);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "删除数据监控设置信息异常："+e.getMessage());
		}
		return result;
	}
	
	public Map saveOrUpdate(MonitorSet monitorSet,String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("数据监控设置信息更新开始：" + monitorSet);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			monitorSet.setModifier(userid);
			//更新
			if(!StringUtils.isEmpty(monitorSet.getGid())) {
				monitorSet.setLast_time(new Date());
				monitorSetMapper.update(monitorSet);
				if(monitorSet.getStopflag()==0) {
					Map m = new HashMap<>();
					m.put("stopflag", 0);
					m.put("setgid", monitorSet.getGid());
					monitorLogMapper.updateStatusByType(m);
				}
				LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "数据监控设置信息更新"+monitorSet);
			}else {//新增
				monitorSet.setCreator(userid);
				monitorSetMapper.insert(monitorSet);
				LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, userid, "新增数据监控设置信息"+monitorSet);
			}
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("保存数据监控设置信息异常" + e);
			LogUtil.log(LogOptTypeEnum.SAVE, LogOptSatusEnum.FAIL, userid, "保存数据监控设置信息异常："+e.getMessage());
		}
		return result;
	}
	
	public List<Map> findAllForPage(MonitorSetQueryVO queryVO, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("查询所有数据监控设置信息开始：" + queryVO);
		}
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询数据监控设置信息，"+queryVO);
		return monitorSetMapper.findAllForPage(queryVO);
	}
	
	public int findAllCount(MonitorSetQueryVO queryVO) {
		return monitorSetMapper.findAllCount(queryVO);
	}
	
}
