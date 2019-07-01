package com.agri.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.agri.monitor.mapper.AnimalsTypeMapper;
import com.agri.monitor.utils.CacheTypeEnum;
import com.agri.monitor.utils.CacheUtil;

@Component
public class ApplicationRunnerImpl implements ApplicationRunner {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationRunnerImpl.class);
	
	@Autowired
	private AnimalsTypeMapper animalsTypeMapper;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("项目启动完成，开始加装缓存数据");
		}
		CacheUtil.putCache(CacheTypeEnum.ANIMALSTYPE, animalsTypeMapper.findAll());
	}

}
