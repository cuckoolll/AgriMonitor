package com.agri.monitor.service.datamanage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agri.monitor.entity.WaterInfo;
import com.agri.monitor.mapper.WaterInfoMapper;

@Service
@Transactional
public class WaterInfoService {
	@Autowired
	private WaterInfoMapper waterInfoMapper;
	
	@Value("${spring.profiles.active}")  
	private String active;  
	
	public List<WaterInfo> findAll() {
		return waterInfoMapper.findAll();
	}
	
	public List<WaterInfo> queryInfoByCountryAndTime(final String county, final String time) {
		return waterInfoMapper.queryInfoByCountryAndTime(county, time);
	}
}
