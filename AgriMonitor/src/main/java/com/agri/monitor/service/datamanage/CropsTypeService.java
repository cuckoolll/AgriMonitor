package com.agri.monitor.service.datamanage;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.agri.monitor.entity.CropsType;
import com.agri.monitor.enums.CacheTypeEnum;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.CropsPlantMapper;
import com.agri.monitor.mapper.CropsTypeMapper;
import com.agri.monitor.utils.CacheUtil;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.vo.CropsTypeQueryVO;

@Service
public class CropsTypeService {
	
	private static final Logger logger = LoggerFactory.getLogger(CropsTypeService.class);
	
	@Autowired
	private CropsTypeMapper cropsTypeMapper;
	@Autowired
	private CropsPlantMapper cropsPlantMapper;
	
	public CropsType findById(Integer gid, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("查询农作物类型信息，GID=" + gid);
		}
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询农作物类型信息，GID="+gid);
		return cropsTypeMapper.findById(gid);
	}
	
	public Map doDel(List<Integer> gids, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("农作物类型删除开始：" + gids);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			//删除前判断是否有养殖场数据关联该分类
			int count = cropsPlantMapper.findByCropsType(gids);
			if(count > 0) {
				result.put("code", -2);
				result.put("msg", "删除的农作物类型已经被引用，只能停用");
				return result;
			}else {
				cropsTypeMapper.delete(gids);
				LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "农作物类型删除，GID="+gids);
				//更新缓存
				CropsTypeQueryVO queryVO = new CropsTypeQueryVO();
				queryVO.setPage(1);
				queryVO.setLimit(Integer.MAX_VALUE);
				CacheUtil.putCache(CacheTypeEnum.CROPSTYPE, cropsTypeMapper.findAllForPage(queryVO));
			}
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除农作物类型异常" + e);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "农作物类型数据删除异常："+e.getMessage());
		}
		return result;
	}
	
	public Map saveOrUpdate(CropsType cropsType,String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("农作物类型数据更新开始：" + cropsType);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			cropsType.setModifier(userid);
			//更新
			if(!StringUtils.isEmpty(cropsType.getGid())) {
				cropsType.setLast_time(new Date());
				cropsTypeMapper.update(cropsType);
				LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "农作物类型数据更新"+cropsType);
			}else {//新增
				cropsType.setCreator(userid);
				cropsTypeMapper.insert(cropsType);
				LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, userid, "新增农作物类型数据"+cropsType);
			}
			//更新缓存
			CropsTypeQueryVO queryVO = new CropsTypeQueryVO();
			queryVO.setPage(1);
			queryVO.setLimit(Integer.MAX_VALUE);
			CacheUtil.putCache(CacheTypeEnum.CROPSTYPE, cropsTypeMapper.findAllForPage(queryVO));
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("保存农作物类型信息异常" + e);
			LogUtil.log(LogOptTypeEnum.SAVE, LogOptSatusEnum.FAIL, userid, "保存农作物类型数据异常："+e.getMessage());
		}
		return result;
	}
	
	public List<CropsType> findAllForPage(CropsTypeQueryVO queryVO, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("查询所有农作物类型数据开始：" + queryVO);
		}
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询农作物类型信息，"+queryVO);
		return cropsTypeMapper.findAllForPage(queryVO);
	}
	
	public int findAllCount(CropsTypeQueryVO queryVO) {
		return cropsTypeMapper.findAllCount(queryVO);
	}
	
	public Map cropstypeQy(List<Integer> gids, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("启用农作物类型：" + gids);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			cropsTypeMapper.cropstypeQy(gids);
			LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "启用农作物类型，"+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("启用农作物类型异常" + e);
			LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.FAIL, userid, "启用农作物类型异常："+e.getMessage());
		}
		return result;
	}
	
	public Map cropstypeTy(List<Integer> gids, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("停用农作物类型：" + gids);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			cropsTypeMapper.cropstypeTy(gids);
			LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "停用农作物类型，"+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("启用农作物类型异常" + e);
			LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.FAIL, userid, "启用农作物类型异常："+e.getMessage());
		}
		return result;
	}
	
}
