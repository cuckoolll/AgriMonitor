package com.agri.monitor.service.datamanage;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.agri.monitor.entity.ProTeamAndEmployInfo;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.ProTeamAndEmployInfoMapper;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.vo.ProTeamAndEmployInfoQueryVO;

@Service
public class ProTeamAndEmployInfoService {
	
	private static final Logger logger = LoggerFactory.getLogger(ProTeamAndEmployInfoService.class);
	
	@Autowired
	private ProTeamAndEmployInfoMapper proTeamAndEmployInfoMapper;
	
	public ProTeamAndEmployInfo findById(Integer gid, String userid) {
		info("获取农业生产组织及从业人员信息，GID=" + gid);
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询农业生产组织及从业人员信息，GID="+gid);
		return proTeamAndEmployInfoMapper.findById(gid);
	}
	
	public Map doDel(List<Integer> gids, String userid) {
		info("农业生产组织及从业人员数据删除开始：" + gids);
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			proTeamAndEmployInfoMapper.delete(gids);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "农业生产组织及从业人员数据删除，GID="+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除农业生产组织及从业人员信息异常" + e);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "农业生产组织及从业人员数据删除异常："+e.getMessage());
		}
		return result;
	}
	
	public Map saveOrUpdate(ProTeamAndEmployInfo info,String userid) {
		info("农业生产组织及从业人员数据更新开始：" + info);
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			info.setModifier(userid);
			//更新
			if(!StringUtils.isEmpty(info.getGid())) {
				info.setLast_time(new Date());
				proTeamAndEmployInfoMapper.update(info);
				LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "农业生产组织及从业人员数据更新"+info);
			}else {//新增
				info.setCreator(userid);
				proTeamAndEmployInfoMapper.insert(info);
				LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, userid, "新增农业生产组织及从业人员数据"+info);
			}
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("保存农业生产组织及从业人员信息异常" + e);
			LogUtil.log(LogOptTypeEnum.SAVE, LogOptSatusEnum.FAIL, userid, "保存农业生产组织及从业人员数据异常："+e.getMessage());
		}
		return result;
	}
	
	public List<Map> findAllForPage(ProTeamAndEmployInfoQueryVO queryVO, String userid) {
		info("查询所有农业生产组织及从业人员数据开始：" + queryVO);
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询农业生产组织及从业人员信息，"+queryVO);
		return proTeamAndEmployInfoMapper.findAllForPage(queryVO);
	}
	
	public int findAllCount(ProTeamAndEmployInfoQueryVO queryVO) {
		return proTeamAndEmployInfoMapper.findAllCount(queryVO);
	}
	
	private void info(String msg) {
		if (logger.isInfoEnabled()) {
			logger.info(msg);
		}
	}
}
