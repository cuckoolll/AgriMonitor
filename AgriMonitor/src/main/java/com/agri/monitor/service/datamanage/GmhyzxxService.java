package com.agri.monitor.service.datamanage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.agri.monitor.entity.Gmhyzxx;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.GmhyzxxMapper;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.vo.GmhyzxxQueryVO;

@Service
public class GmhyzxxService {
	
	private static final Logger logger = LoggerFactory.getLogger(GmhyzxxService.class);
	
	@Autowired
	private GmhyzxxMapper gmhyzxxMapper;
	
	public Gmhyzxx findById(Integer gid, String userid) {
		info("获取规模化养殖信息，GID=" + gid);
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询规模化养殖信息，GID="+gid);
		return gmhyzxxMapper.findById(gid);
	}
	
	public Map doDel(List<Integer> gids, String userid) {
		info("规模化养殖数据删除开始：" + gids);
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			gmhyzxxMapper.delete(gids);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "规模化养殖数据删除，GID="+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除规模化养殖信息异常" + e);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "规模化养殖数据删除异常："+e.getMessage());
		}
		return result;
	}
	
	public Map saveOrUpdate(Gmhyzxx info,String userid) {
		info("规模化养殖数据更新开始：" + info);
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			//更新
			if(!StringUtils.isEmpty(info.getGid())) {
				gmhyzxxMapper.update(info);
				LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "规模化养殖数据更新"+info);
			}else {//新增
				gmhyzxxMapper.insert(info);
				LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, userid, "新增规模化养殖数据"+info);
			}
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("保存规模化养殖信息异常" + e);
			LogUtil.log(LogOptTypeEnum.SAVE, LogOptSatusEnum.FAIL, userid, "保存规模化养殖数据异常："+e.getMessage());
		}
		return result;
	}
	
	public List<Map> findAllForPage(GmhyzxxQueryVO queryVO, String userid) {
		info("查询所有规模化养殖数据开始：" + queryVO);
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询规模化养殖信息，"+queryVO);
		return gmhyzxxMapper.findAllForPage(queryVO);
	}
	
	public int findAllCount(GmhyzxxQueryVO queryVO) {
		return gmhyzxxMapper.findAllCount(queryVO);
	}
	
	private void info(String msg) {
		if (logger.isInfoEnabled()) {
			logger.info(msg);
		}
	}
}
