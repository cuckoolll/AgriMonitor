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

import com.agri.monitor.entity.AnimalsTarget;
import com.agri.monitor.enums.CacheTypeEnum;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.AnimalsBreedMapper;
import com.agri.monitor.mapper.AnimalsTargetMapper;
import com.agri.monitor.utils.CacheUtil;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.vo.AnimalsTargetQueryVO;

@Service
public class AnimalsTargetService {
	
	private static final Logger logger = LoggerFactory.getLogger(AnimalsTargetService.class);
	
	@Autowired
	private AnimalsTargetMapper animalsTargetMapper;
	@Autowired
	private AnimalsBreedMapper animalsBreedMapper;
	
	public AnimalsTarget findById(Integer gid, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("查询畜牧业指标信息，GID=" + gid);
		}
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询畜牧业指标信息，GID="+gid);
		return animalsTargetMapper.findById(gid);
	}
	
	public Map doDel(List<Integer> gids, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("畜牧业指标删除开始：" + gids);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			//删除前判断是否有数据关联该分类
			int count = animalsBreedMapper.findByAnimalsTarget(gids);
			if(count > 0) {
				result.put("code", -2);
				result.put("msg", "删除的畜牧业指标已经被引用，只能停用");
				return result;
			}else {
				animalsTargetMapper.delete(gids);
				LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "畜牧业指标删除，GID="+gids);
				//更新缓存
				AnimalsTargetQueryVO queryVO = new AnimalsTargetQueryVO();
				queryVO.setPage(1);
				queryVO.setLimit(Integer.MAX_VALUE);
				CacheUtil.putCache(CacheTypeEnum.ANIMALSTARGET, animalsTargetMapper.findAllForPage(queryVO));
			}
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除畜牧业指标异常" + e);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "畜牧业指标数据删除异常："+e.getMessage());
		}
		return result;
	}
	
	public Map saveOrUpdate(AnimalsTarget animalsTarget,String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("畜牧业指标数据更新开始：" + animalsTarget);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			animalsTarget.setModifier(userid);
			//更新
			if(!StringUtils.isEmpty(animalsTarget.getGid())) {
				animalsTarget.setLast_time(new Date());
				animalsTargetMapper.update(animalsTarget);
				LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "畜牧业指标数据更新"+animalsTarget);
			}else {//新增
				animalsTarget.setCreator(userid);
				animalsTargetMapper.insert(animalsTarget);
				LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, userid, "新增畜牧业指标数据"+animalsTarget);
			}
			//更新缓存
			AnimalsTargetQueryVO queryVO = new AnimalsTargetQueryVO();
			queryVO.setPage(1);
			queryVO.setLimit(Integer.MAX_VALUE);
			CacheUtil.putCache(CacheTypeEnum.ANIMALSTARGET, animalsTargetMapper.findAllForPage(queryVO));
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("保存畜牧业指标信息异常" + e);
			LogUtil.log(LogOptTypeEnum.SAVE, LogOptSatusEnum.FAIL, userid, "保存畜牧业指标数据异常："+e.getMessage());
		}
		return result;
	}
	
	public List<AnimalsTarget> findAllForPage(AnimalsTargetQueryVO queryVO, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("查询所有畜牧业指标数据开始：" + queryVO);
		}
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询畜牧业指标信息，"+queryVO);
		return animalsTargetMapper.findAllForPage(queryVO);
	}
	
	public int findAllCount(AnimalsTargetQueryVO queryVO) {
		return animalsTargetMapper.findAllCount(queryVO);
	}
	
	public Map animalsTargetQy(List<Integer> gids, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("启用畜牧业指标：" + gids);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			animalsTargetMapper.animalsTargetQy(gids);
			LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "启用畜牧业指标，"+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("启用畜牧业指标异常" + e);
			LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.FAIL, userid, "启用畜牧业指标异常："+e.getMessage());
		}
		return result;
	}
	
	public Map animalsTargetTy(List<Integer> gids, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("停用畜牧业指标：" + gids);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			animalsTargetMapper.animalsTargetTy(gids);
			LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "停用畜牧业指标，"+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("启用畜牧业指标异常" + e);
			LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.FAIL, userid, "启用畜牧业指标异常："+e.getMessage());
		}
		return result;
	}
	
}
