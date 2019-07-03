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

import com.agri.monitor.entity.AnimalsType;
import com.agri.monitor.enums.CacheTypeEnum;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.AnimalsTypeMapper;
import com.agri.monitor.mapper.FarmInfoMapper;
import com.agri.monitor.utils.CacheUtil;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.vo.AnimalsTypeQueryVO;

@Service
public class AnimalsTypeService {
	
	private static final Logger logger = LoggerFactory.getLogger(AnimalsTypeService.class);
	
	@Autowired
	private AnimalsTypeMapper animalsTypeMapper;
	@Autowired
	private FarmInfoMapper farmInfoMapper;
	
	public AnimalsType findById(Integer gid, Integer userid) {
		if (logger.isInfoEnabled()) {
			logger.info("查询认定畜种信息，GID=" + gid);
		}
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询认定畜种信息，GID="+gid);
		return animalsTypeMapper.findById(gid);
	}
	
	public Map doDel(List<Integer> gids, Integer userid) {
		if (logger.isInfoEnabled()) {
			logger.info("认定畜种删除开始：" + gids);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			//删除前判断是否有养殖场数据关联该分类
			int count = farmInfoMapper.findByAnimalsType(gids);
			if(count > 0) {
				result.put("code", -2);
				result.put("msg", "删除的认定畜种已经被引用，只能停用");
				return result;
			}else {
				animalsTypeMapper.delete(gids);
				LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "认定畜种删除，GID="+gids);
				//更新缓存
				AnimalsTypeQueryVO queryVO = new AnimalsTypeQueryVO();
				queryVO.setPage(1);
				queryVO.setLimit(Integer.MAX_VALUE);
				CacheUtil.putCache(CacheTypeEnum.ANIMALSTYPE, animalsTypeMapper.findAllForPage(queryVO));
			}
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除认定畜种异常" + e);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "认定畜种数据删除异常："+e.getMessage());
		}
		return result;
	}
	
	public Map saveOrUpdate(AnimalsType animalsType,Integer userid) {
		if (logger.isInfoEnabled()) {
			logger.info("认定畜种数据更新开始：" + animalsType);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			animalsType.setModifier(userid);
			//更新
			if(!StringUtils.isEmpty(animalsType.getGid())) {
				animalsType.setLast_time(new Date());
				animalsTypeMapper.update(animalsType);
				LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "认定畜种数据更新"+animalsType);
			}else {//新增
				animalsType.setCreator(userid);
				animalsTypeMapper.insert(animalsType);
				LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, userid, "新增认定畜种数据"+animalsType);
			}
			//更新缓存
			AnimalsTypeQueryVO queryVO = new AnimalsTypeQueryVO();
			queryVO.setPage(1);
			queryVO.setLimit(Integer.MAX_VALUE);
			CacheUtil.putCache(CacheTypeEnum.ANIMALSTYPE, animalsTypeMapper.findAllForPage(queryVO));
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("保存认定畜种信息异常" + e);
			LogUtil.log(LogOptTypeEnum.SAVE, LogOptSatusEnum.FAIL, userid, "保存认定畜种数据异常："+e.getMessage());
		}
		return result;
	}
	
	public List<AnimalsType> findAllForPage(AnimalsTypeQueryVO queryVO, Integer userid) {
		if (logger.isInfoEnabled()) {
			logger.info("查询所有认定畜种数据开始：" + queryVO);
		}
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询认定畜种信息，"+queryVO);
		return animalsTypeMapper.findAllForPage(queryVO);
	}
	
	public int findAllCount(AnimalsTypeQueryVO queryVO) {
		return animalsTypeMapper.findAllCount(queryVO);
	}
	
	public Map animalstypeQy(List<Integer> gids, Integer userid) {
		if (logger.isInfoEnabled()) {
			logger.info("启用认定畜种：" + gids);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			animalsTypeMapper.animalstypeQy(gids);
			LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "启用认定畜种，"+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("启用认定畜种异常" + e);
			LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.FAIL, userid, "启用认定畜种异常："+e.getMessage());
		}
		return result;
	}
	
	public Map animalstypeTy(List<Integer> gids, Integer userid) {
		if (logger.isInfoEnabled()) {
			logger.info("停用认定畜种：" + gids);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			animalsTypeMapper.animalstypeTy(gids);
			LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "停用认定畜种，"+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("启用认定畜种异常" + e);
			LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.FAIL, userid, "启用认定畜种异常："+e.getMessage());
		}
		return result;
	}
	
}
