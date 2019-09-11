package com.agri.monitor.service.datamanage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.agri.monitor.entity.AgriInputinfo;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.AgriInputinfoMapper;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.vo.AgriInputinfoQueryVO;

@Service
public class AgriInputinfoService {
	
	private static final Logger logger = LoggerFactory.getLogger(AgriInputinfoService.class);
	
	@Autowired
	private AgriInputinfoMapper agriInputinfoMapper;
	
	public AgriInputinfo findById(Integer gid, String userid) {
		info("获取农业投入信息，GID=" + gid);
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询农业投入信息，GID="+gid);
		return agriInputinfoMapper.findById(gid);
	}
	
	public Map doDel(List<Integer> gids, String userid) {
		info("农业投入数据删除开始：" + gids);
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			agriInputinfoMapper.delete(gids);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "农业投入数据删除，GID="+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除农业投入信息异常" + e);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "农业投入数据删除异常："+e.getMessage());
		}
		return result;
	}
	
	public Map saveOrUpdate(AgriInputinfo info,String userid) {
		info("农业投入数据更新开始：" + info);
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			//更新
			if(!StringUtils.isEmpty(info.getGid())) {
				agriInputinfoMapper.update(info);
				LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "农业投入数据更新"+info);
			}else {//新增
				agriInputinfoMapper.insert(info);
				LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, userid, "新增农业投入数据"+info);
			}
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("保存农业投入信息异常" + e);
			LogUtil.log(LogOptTypeEnum.SAVE, LogOptSatusEnum.FAIL, userid, "保存农业投入数据异常："+e.getMessage());
		}
		return result;
	}
	
	public List<Map> findAllForPage(AgriInputinfoQueryVO queryVO, String userid) {
		info("查询所有农业投入数据开始：" + queryVO);
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询农业投入信息，"+queryVO);
		return agriInputinfoMapper.findAllForPage(queryVO);
	}
	
	public int findAllCount(AgriInputinfoQueryVO queryVO) {
		return agriInputinfoMapper.findAllCount(queryVO);
	}
	
	private void info(String msg) {
		if (logger.isInfoEnabled()) {
			logger.info(msg);
		}
	}
}
