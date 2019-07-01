package com.agri.monitor.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.Animalstype;

@Repository
public interface AnimalsTypeMapper {
	
	List<Animalstype> findAll();
	
}
