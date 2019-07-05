package com.agri.monitor.service.datamanage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.agri.monitor.entity.AnimalsBreed;
import com.agri.monitor.entity.AnimalsTarget;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.CacheTypeEnum;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.AnimalsBreedMapper;
import com.agri.monitor.utils.CacheUtil;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.vo.AnimalsBreedQueryVO;

@Service
public class AnimalsBreedService {
	
	private static final Logger logger = LoggerFactory.getLogger(AnimalsBreedService.class);
	
	@Autowired
	private AnimalsBreedMapper animalsBreedMapper;
	
	public AnimalsBreed findById(Integer gid, Integer userid) {
		if (logger.isInfoEnabled()) {
			logger.info("获取养殖信息，GID=" + gid);
		}
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询畜牧业生产情况信息，GID="+gid);
		return animalsBreedMapper.findById(gid);
	}
	
	public Map doDel(List<Integer> gids, Integer userid) {
		if (logger.isInfoEnabled()) {
			logger.info("畜牧业生产情况数据删除开始：" + gids);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			animalsBreedMapper.delete(gids);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "畜牧业生产情况数据删除，GID="+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除畜牧业生产情况信息异常" + e);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "畜牧业生产情况数据删除异常："+e.getMessage());
		}
		return result;
	}
	
	public Map saveOrUpdate(AnimalsBreed animalsBreed,Integer userid) {
		if (logger.isInfoEnabled()) {
			logger.info("畜牧业生产情况数据更新开始：" + animalsBreed);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			animalsBreed.setModifier(userid);
			//更新
			if(!StringUtils.isEmpty(animalsBreed.getGid())) {
				animalsBreed.setLast_time(new Date());
				animalsBreedMapper.update(animalsBreed);
				LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "畜牧业生产情况数据更新"+animalsBreed);
			}else {//新增
				animalsBreed.setCreator(userid);
				animalsBreedMapper.insert(animalsBreed);
				LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, userid, "新增畜牧业生产情况数据"+animalsBreed);
			}
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("保存畜牧业生产情况信息异常" + e);
			LogUtil.log(LogOptTypeEnum.SAVE, LogOptSatusEnum.FAIL, userid, "保存畜牧业生产情况数据异常："+e.getMessage());
		}
		return result;
	}
	
	public List<Map> findAllForPage(AnimalsBreedQueryVO queryVO, Integer userid) {
		if (logger.isInfoEnabled()) {
			logger.info("查询所有畜牧业生产情况数据开始：" + queryVO);
		}
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询畜牧业生产情况信息，"+queryVO);
		return animalsBreedMapper.findAllForPage(queryVO);
	}
	
	public int findAllCount(AnimalsBreedQueryVO queryVO) {
		return animalsBreedMapper.findAllCount(queryVO);
	}
	
	public Map dataImport(MultipartFile file, HttpServletRequest request) {
		if (logger.isInfoEnabled()) {
			logger.info("畜牧业生产情况数据导入开始");
		}
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		result.put("msg", "成功");
		Workbook wb = null; // 工作区域
		try {
			//获取文件名
	        String filename=file.getOriginalFilename();
	        // 获取文件后缀
	        String prefix=filename.substring(filename.lastIndexOf(".")+1);
	        if (logger.isInfoEnabled()) {
				logger.info("畜牧业生产情况数据导入，文件名：" + filename);
			}
	        if (prefix.equals("xlsx")) {
	        	wb = new XSSFWorkbook(file.getInputStream());
	        } else if (prefix.equals("xls")) {
	        	wb = new HSSFWorkbook(file.getInputStream());
	        } else {
	        	if (logger.isInfoEnabled()) {
					logger.info("畜牧业生产情况数据导入，不支持的文件类型");
				}
	        	result.put("code", -1);
	    		result.put("msg", "不支持的文件类型");
	    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(),"不支持的文件类型");
	    		return result;
	        }
	        
	        Sheet sheet1 = wb.getSheetAt(0);
	        int i = 0;
	        String towns = null;
	        Integer ym = null;
	        List<AnimalsBreed> list = new ArrayList<>();
	        for (Row row : sheet1) {
	        	//解析所属乡镇
	           if (i == 1) {
	        	   towns = row.getCell(1).getStringCellValue();
	        	   //TODO 与缓存中乡镇信息对比
	        	   
	        	   String ymstr  = row.getCell(3).getStringCellValue();
	        	   if(StringUtils.isEmpty(ymstr)) {
	        		   result.put("code", -1);
	        		   result.put("msg", "报表年月不符合格式，请按照201908填写");
	        		   return result;
	        	   }
	        	   ymstr=ymstr.trim();
	        	    try {
	        	    	ym=Integer.parseInt(ymstr);
					} catch (Exception e) {
						result.put("code", -1);
		        		result.put("msg", "报表年月不符合格式，请按照201908填写");
		        		return result;
					}
	           }
	           if (i >= 4) {
	        	   AnimalsBreed animalsBreed = new AnimalsBreed();
	        	   String name = row.getCell(0).getStringCellValue();
	        	   if(StringUtils.isEmpty(name)) {
	        		   result.put("code", -1);
	        		   result.put("msg", "第"+(i+1)+"行畜牧业指标未填写");
	        		   return result;
	        	   }
	        	   name=name.trim();
	        	   Integer type = getAnimalsTarget(name);
	        	   if(null == type) {
	        		   result.put("code", -1);
	        		   result.put("msg", "第"+(i+1)+"行畜牧业指标在系统中未维护");
	        		   return result;
	        	   }
	        	   animalsBreed.setAnimals_target(type);
	        	   animalsBreed.setDate_month(ym);
	        	   animalsBreed.setCreator(user.getUser_id());
	        	   animalsBreed.setModifier(user.getUser_id());
	        	   animalsBreed.setCounty("刚察县");
	        	   animalsBreed.setTowns(towns);
	        	   animalsBreed.setSurplus_size(row.getCell(1).getNumericCellValue());
	        	   animalsBreed.setFemale_size(row.getCell(2).getNumericCellValue());
	        	   animalsBreed.setChild_size(row.getCell(3).getNumericCellValue());
	        	   animalsBreed.setSurvival_size(row.getCell(4).getNumericCellValue());
	        	   animalsBreed.setDeath_size(row.getCell(5).getNumericCellValue());
	        	   animalsBreed.setMaturity_size(row.getCell(6).getNumericCellValue());
	        	   animalsBreed.setSell_size(row.getCell(7).getNumericCellValue());
	        	   animalsBreed.setMeat_output(row.getCell(8).getNumericCellValue());
	        	   animalsBreed.setMilk_output(row.getCell(9).getNumericCellValue());
	        	   animalsBreed.setEgg_output(row.getCell(10).getNumericCellValue());
	        	   animalsBreed.setHair_output(row.getCell(11).getNumericCellValue());
	        	   list.add(animalsBreed);
	           }
	           i++;
	        }
	        animalsBreedMapper.batchInsert(list);
	        LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.SUCESS, user.getUser_id(), "导入查询畜牧业生产情况信息，共导入"+list.size()+"条");
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("畜牧业生产情况数据导入，解析文件异常", e);
			}
			result.put("code", -1);
    		result.put("msg", "解析文件失败");
    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "导入畜牧业生产情况信息异常："+e.getMessage());
		}
		return result;
	}
	
	private Integer getAnimalsTarget(String name) {
		if (StringUtils.isEmpty(name)) {
			return null;
		}
		List<AnimalsTarget> list = (List<AnimalsTarget>) CacheUtil.getCache(CacheTypeEnum.ANIMALSTARGET);
		if (list == null || list.size() == 0) {
			return null;
		}
		for (AnimalsTarget type : list) {
			if(name.equals(type.getTarget_name())) {
				return type.getGid();
			}
		}
		return null;
	}
}
