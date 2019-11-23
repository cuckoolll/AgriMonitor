package com.agri.monitor.service.datamanage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agri.monitor.entity.Areainfo;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.AreainfoMapper;
import com.agri.monitor.utils.LogUtil;

@Service
public class AreainfoService {
	
	private static final Logger logger = LoggerFactory.getLogger(AreainfoService.class);
	
	@Autowired
	private AreainfoMapper areainfoMapper;
	
	public Areainfo findById(Integer gid, String userid) {
		info("获取行政信息，GID=" + gid);
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询行政信息，GID="+gid);
		return areainfoMapper.findById(gid);
	}
	
	public Map update(Areainfo info,String userid) {
		info("行政数据更新开始：" + info);
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			areainfoMapper.update(info);
			LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "行政数据更新"+info);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("保存行政信息异常" + e);
			LogUtil.log(LogOptTypeEnum.SAVE, LogOptSatusEnum.FAIL, userid, "保存行政数据异常："+e.getMessage());
		}
		return result;
	}
	
	public List<Map> findAll(String userid) {
		info("查询所有行政数据开始：");
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "");
		return areainfoMapper.findAll();
	}
	
	private void info(String msg) {
		if (logger.isInfoEnabled()) {
			logger.info(msg);
		}
	}
}
