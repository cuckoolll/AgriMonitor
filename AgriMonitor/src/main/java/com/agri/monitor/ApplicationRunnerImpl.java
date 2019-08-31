package com.agri.monitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.agri.monitor.enums.CacheTypeEnum;
import com.agri.monitor.mapper.AnimalsTargetMapper;
import com.agri.monitor.mapper.AnimalsTypeMapper;
import com.agri.monitor.mapper.CropsTypeMapper;
import com.agri.monitor.mapper.UserinfoMapper;
import com.agri.monitor.service.agriinfo.AgriNewsService;
import com.agri.monitor.service.datamanage.ClimateInfoService;
import com.agri.monitor.service.datamanage.GrassInfoService;
import com.agri.monitor.service.datamanage.SoilInfoService;
import com.agri.monitor.utils.CacheUtil;
import com.agri.monitor.vo.AnimalsTargetQueryVO;
import com.agri.monitor.vo.AnimalsTypeQueryVO;
import com.agri.monitor.vo.CropsTypeQueryVO;
import com.agri.monitor.vo.UserQueryVO;

@Component
public class ApplicationRunnerImpl implements ApplicationRunner {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationRunnerImpl.class);
	
	@Autowired
	private AnimalsTypeMapper animalsTypeMapper;
	
	@Autowired
	private AnimalsTargetMapper animalsTargetMapper;
	
	@Autowired
	private ClimateInfoService climateInfoService;
	
	@Autowired
	private CropsTypeMapper cropsTypeMapper;
	
	@Autowired
	private SoilInfoService soilInfoService;
	
	@Autowired
	private UserinfoMapper userinfoMapper;
	
	@Autowired
	private GrassInfoService grassInfoService;
	
	@Autowired
	private AgriNewsService agriNewsService;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("项目启动完成，开始加装缓存数据");
		}
		//缓存认定畜种
		AnimalsTypeQueryVO queryVO = new AnimalsTypeQueryVO();
		queryVO.setPage(1);
		queryVO.setLimit(Integer.MAX_VALUE);
		CacheUtil.putCache(CacheTypeEnum.ANIMALSTYPE, animalsTypeMapper.findAllForPage(queryVO));
		//缓存畜牧业指标
		AnimalsTargetQueryVO queryVO1 = new AnimalsTargetQueryVO();
		queryVO.setPage(1);
		queryVO.setLimit(Integer.MAX_VALUE);
		CacheUtil.putCache(CacheTypeEnum.ANIMALSTARGET, animalsTargetMapper.findAllForPage(queryVO1));
		//缓存乡镇信息
		CacheUtil.putCache(CacheTypeEnum.TOWNS, Arrays.asList(new String[]{"沙柳河镇","哈尔盖镇","伊克乌兰乡","泉吉乡","吉尔孟乡"}));
		//缓存气候指标信息
		CacheUtil.putCache(CacheTypeEnum.CLIMATEINDEX, climateInfoService.getClimateIndex());
		//缓存农作物类型
		CropsTypeQueryVO queryVO2 = new CropsTypeQueryVO();
		queryVO2.setPage(1);
		queryVO2.setLimit(Integer.MAX_VALUE);
		CacheUtil.putCache(CacheTypeEnum.CROPSTYPE, cropsTypeMapper.findAllForPage(queryVO2));
		//缓存土壤指标
		CacheUtil.putCache(CacheTypeEnum.SOILINDEX, soilInfoService.getSoilIndex());
		//用户角色
		CacheUtil.putCache(CacheTypeEnum.USER_ROLE, userinfoMapper.queryUserRole());
		//用户
		UserQueryVO queryVO3 = new UserQueryVO();
		queryVO3.setPage(1);
		queryVO3.setLimit(Integer.MAX_VALUE);
		CacheUtil.putCache(CacheTypeEnum.USER, userinfoMapper.findAllForPage(queryVO3));
		//缓存草原生态指标
		CacheUtil.putCache(CacheTypeEnum.GRASSINDEX, grassInfoService.getGrassIndex());
		//缓存公示文件类别
		CacheUtil.putCache(CacheTypeEnum.INFOTYPE, agriNewsService.getInfoType());
	}

}
