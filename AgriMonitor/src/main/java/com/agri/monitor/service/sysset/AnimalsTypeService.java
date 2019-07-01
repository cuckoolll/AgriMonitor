package com.agri.monitor.service.sysset;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agri.monitor.entity.Animalstype;
import com.agri.monitor.mapper.AnimalsTypeMapper;

@Service
public class AnimalsTypeService {
	
	private static final Logger logger = LoggerFactory.getLogger(AnimalsTypeService.class);
	
	@Autowired
	private AnimalsTypeMapper animalsTypeMapper;
	
	public List<Animalstype> findAll(){
		return animalsTypeMapper.findAll();
	}
}
