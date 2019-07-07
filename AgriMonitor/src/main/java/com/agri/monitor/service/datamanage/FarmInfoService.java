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

import com.agri.monitor.entity.AnimalsType;
import com.agri.monitor.entity.FarmInfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.CacheTypeEnum;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.FarmInfoMapper;
import com.agri.monitor.utils.CacheUtil;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.utils.UrbanAreaUtil;
import com.agri.monitor.vo.FarmQueryVO;

@Service
public class FarmInfoService {
	
	private static final Logger logger = LoggerFactory.getLogger(FarmInfoService.class);
	
	@Autowired
	private FarmInfoMapper farmInfoMapper;
	
	public FarmInfo findById(Integer gid, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("获取养殖信息，GID=" + gid);
		}
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询养殖场信息，GID="+gid);
		return farmInfoMapper.findById(gid);
	}
	
	public Map doDel(List<Integer> gids, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("养殖场数据删除开始：" + gids);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			farmInfoMapper.delete(gids);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "养殖场数据删除，GID="+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除养殖场信息异常" + e);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "养殖场数据删除异常："+e.getMessage());
		}
		return result;
	}
	
	public Map saveOrUpdate(FarmInfo farminfo,String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("养殖场数据更新开始：" + farminfo);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			farminfo.setModifier(userid);
			//更新
			if(!StringUtils.isEmpty(farminfo.getGid())) {
				farminfo.setLast_time(new Date());
				farmInfoMapper.update(farminfo);
				LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "养殖场数据更新"+farminfo);
			}else {//新增
				farminfo.setCreator(userid);
				farmInfoMapper.insert(farminfo);
				LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, userid, "新增养殖场数据"+farminfo);
			}
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("保存养殖场信息异常" + e);
			LogUtil.log(LogOptTypeEnum.SAVE, LogOptSatusEnum.FAIL, userid, "保存养殖场数据异常："+e.getMessage());
		}
		return result;
	}
	
	public List<Map> findAllForPage(FarmQueryVO farmQueryVO, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("查询所有养殖场数据开始：" + farmQueryVO);
		}
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询养殖场信息，"+farmQueryVO);
		return farmInfoMapper.findAllForPage(farmQueryVO);
	}
	
	public int findAllCount(FarmQueryVO farmQueryVO) {
		return farmInfoMapper.findAllCount(farmQueryVO);
	}
	
	public Map dataImport(MultipartFile file, HttpServletRequest request) {
		if (logger.isInfoEnabled()) {
			logger.info("养殖场数据导入开始");
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
				logger.info("养殖场数据导入，文件名：" + filename);
			}
	        if (prefix.equals("xlsx")) {
	        	wb = new XSSFWorkbook(file.getInputStream());
	        } else if (prefix.equals("xls")) {
	        	wb = new HSSFWorkbook(file.getInputStream());
	        } else {
	        	if (logger.isInfoEnabled()) {
					logger.info("养殖场数据导入，不支持的文件类型");
				}
	        	result.put("code", -1);
	    		result.put("msg", "不支持的文件类型");
	    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(),"不支持的文件类型");
	    		return result;
	        }
	        
	        Sheet sheet1 = wb.getSheetAt(0);
	        int i = 0;
	        String towns = null;
	        List<FarmInfo> list = new ArrayList<>();
	        for (Row row : sheet1) {
	        	//解析所属乡镇
	           if (i == 1) {
	        	   towns = row.getCell(1).getStringCellValue();
	        	   if (!UrbanAreaUtil.isLegalTown(towns)) {
	        		   result.put("code", -1);
	        		   result.put("msg", "报表乡镇填写错误，请重新选择所属乡镇");
	        		   return result;
	        	   }
	           }
	           if (i >= 3) {
	        	   FarmInfo farminfo = new FarmInfo();
	        	   farminfo.setFarm_name(row.getCell(0).getStringCellValue());
	        	   farminfo.setFarm_address(row.getCell(1).getStringCellValue());
	        	   farminfo.setLegal_person(row.getCell(2).getStringCellValue());
	        	   farminfo.setPhone_num(row.getCell(3).getStringCellValue());
	        	   String name=row.getCell(4).getStringCellValue();
	        	   if(StringUtils.isEmpty(name)) {
	        		   result.put("code", -1);
	        		   result.put("msg", "第"+(i+1)+"行认定畜种未填写");
	        		   return result;
	        	   }
	        	   name=name.trim();
	        	   Integer type = getAnimalsType(name);
	        	   if(null == type) {
	        		   result.put("code", -1);
	        		   result.put("msg", "第"+(i+1)+"行认定畜种在系统中未维护");
	        		   return result;
	        	   }
	        	   farminfo.setAnimals_type(type);
	        	   farminfo.setAnimals_size(Integer.valueOf(row.getCell(5).getStringCellValue()));
	        	   farminfo.setRemarks(row.getCell(6).getStringCellValue());
	        	   farminfo.setCreator(user.getUser_id());
	        	   farminfo.setModifier(user.getUser_id());
	        	   farminfo.setCounty("刚察县");
	        	   farminfo.setTowns(towns);
	        	   list.add(farminfo);
	           }
	           i++;
	        }
	        farmInfoMapper.batchInsert(list);
	        LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.SUCESS, user.getUser_id(), "导入养殖场信息，共导入"+list.size()+"条");
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("养殖场数据导入，解析文件异常", e);
			}
			result.put("code", -1);
    		result.put("msg", "解析文件失败");
    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "导入养殖场信息异常："+e.getMessage());
		}
		return result;
	}
	
	private Integer getAnimalsType(String name) {
		if (StringUtils.isEmpty(name)) {
			return null;
		}
		List<Map> list = (List<Map>) CacheUtil.getCache(CacheTypeEnum.ANIMALSTYPE);
		if (list == null || list.size() == 0) {
			return null;
		}
		for (Map map : list) {
			if(name.equals(map.get("type_name"))) {
				return (Integer) map.get("gid");
			}
		}
		return null;
	}
}
