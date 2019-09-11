package com.agri.monitor.service.datamanage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.agri.monitor.entity.LandResource;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.LandResourceMapper;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.vo.LandResourceQueryVO;

@Service
public class LandResourceService {
	
	private static final Logger logger = LoggerFactory.getLogger(LandResourceService.class);
	
	@Autowired
	private LandResourceMapper landResourceMapper;
	
	public LandResource findById(Integer gid, String userid) {
		info("获取土地资源信息，GID=" + gid);
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询土地资源信息，GID="+gid);
		return landResourceMapper.findById(gid);
	}
	
	public Map doDel(List<Integer> gids, String userid) {
		info("土地资源数据删除开始：" + gids);
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			landResourceMapper.delete(gids);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "土地资源数据删除，GID="+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除土地资源信息异常" + e);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "土地资源数据删除异常："+e.getMessage());
		}
		return result;
	}
	
	public Map saveOrUpdate(LandResource info,String userid) {
		info("土地资源数据更新开始：" + info);
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			//更新
			if(!StringUtils.isEmpty(info.getGid())) {
				landResourceMapper.update(info);
				LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "土地资源数据更新"+info);
			}else {//新增
				landResourceMapper.insert(info);
				LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, userid, "新增土地资源数据"+info);
			}
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("保存土地资源信息异常" + e);
			LogUtil.log(LogOptTypeEnum.SAVE, LogOptSatusEnum.FAIL, userid, "保存土地资源数据异常："+e.getMessage());
		}
		return result;
	}
	
	public List<Map> findAllForPage(LandResourceQueryVO queryVO, String userid) {
		info("查询所有土地资源数据开始：" + queryVO);
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询土地资源信息，"+queryVO);
		return landResourceMapper.findAllForPage(queryVO);
	}
	
	public int findAllCount(LandResourceQueryVO queryVO) {
		return landResourceMapper.findAllCount(queryVO);
	}
	
	private void info(String msg) {
		if (logger.isInfoEnabled()) {
			logger.info(msg);
		}
	}
}
