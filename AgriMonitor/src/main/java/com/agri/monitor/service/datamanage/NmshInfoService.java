package com.agri.monitor.service.datamanage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.agri.monitor.entity.NmshInfo;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.NmshInfoMapper;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.vo.NmshInfoQueryVO;

@Service
public class NmshInfoService {
	
	private static final Logger logger = LoggerFactory.getLogger(NmshInfoService.class);
	
	@Autowired
	private NmshInfoMapper nmshInfoMapper;
	
	public NmshInfo findById(Integer gid, String userid) {
		info("获取农名生活信息，GID=" + gid);
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询农名生活信息，GID="+gid);
		return nmshInfoMapper.findById(gid);
	}
	
	public Map doDel(List<Integer> gids, String userid) {
		info("农名生活数据删除开始：" + gids);
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			nmshInfoMapper.delete(gids);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "农名生活数据删除，GID="+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除农名生活信息异常" + e);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "农名生活数据删除异常："+e.getMessage());
		}
		return result;
	}
	
	public Map saveOrUpdate(NmshInfo info,String userid) {
		info("农名生活数据更新开始：" + info);
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			//更新
			if(!StringUtils.isEmpty(info.getGid())) {
				nmshInfoMapper.update(info);
				LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "农名生活数据更新"+info);
			}else {//新增
				nmshInfoMapper.insert(info);
				LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, userid, "新增农名生活数据"+info);
			}
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("保存农名生活信息异常" + e);
			LogUtil.log(LogOptTypeEnum.SAVE, LogOptSatusEnum.FAIL, userid, "保存农名生活数据异常："+e.getMessage());
		}
		return result;
	}
	
	public List<Map> findAllForPage(NmshInfoQueryVO queryVO, String userid) {
		info("查询所有农名生活数据开始：" + queryVO);
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询农名生活信息，"+queryVO);
		return nmshInfoMapper.findAllForPage(queryVO);
	}
	
	public int findAllCount(NmshInfoQueryVO queryVO) {
		return nmshInfoMapper.findAllCount(queryVO);
	}
	
	private void info(String msg) {
		if (logger.isInfoEnabled()) {
			logger.info(msg);
		}
	}
}
