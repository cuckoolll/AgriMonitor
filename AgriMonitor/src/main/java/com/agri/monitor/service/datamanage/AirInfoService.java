package com.agri.monitor.service.datamanage;

import java.text.SimpleDateFormat;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.agri.monitor.entity.AirInfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.AirInfoMapper;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.vo.AirQueryVO;

@Service
@Transactional
public class AirInfoService {
	
	private static final Logger logger = LoggerFactory.getLogger(AirInfoService.class);
	
	private SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
	
	@Autowired
	private AirInfoMapper airInfoMapper;
	 
	public Map queryInfoByCountryAndTimeForPage(AirQueryVO queryVo, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("获取空气站监测信息，入参=city:" + queryVo.getCity() 
				+ ", quality_time:" + queryVo.getQuality_time());
		}

		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		result.put("msg", "成功");
		result.put("count", airInfoMapper.queryInfoCount(queryVo));
		result.put("data", airInfoMapper.queryInfoByCountryAndTimeForPage(queryVo));
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, 
				"获取空气站监测信息，入参=city:" + queryVo.getCity() + ", quality_time:" + queryVo.getQuality_time());
		return result;
	}
	
	public AirInfo findById(Integer gid, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("获取空气站监测信息，GID=" + gid);
		}
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询空气站监测信息，GID="+gid);
		return airInfoMapper.findById(gid);
	}
	
	public Map saveOrUpdate(AirInfo airinfo, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("空气站监测数据更新开始：" + airinfo);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			airinfo.setModifier(String.valueOf(userid));
			//更新
			if(!StringUtils.isEmpty(airinfo.getGid())) {
				airInfoMapper.updateInfo(airinfo);
				LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "空气站监测数据更新"+airinfo);
			}else {//新增
				airinfo.setCreator(String.valueOf(userid));
				airInfoMapper.insertInfo(airinfo);
				LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, userid, "新增空气站监测数据"+airinfo);
			}
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("保存空气站监测数据异常" + e);
			LogUtil.log(LogOptTypeEnum.SAVE, LogOptSatusEnum.FAIL, userid, "保存空气站监测数据异常："+e.getMessage());
		}
		return result;
	}
	
	public Map delInfoByGid(List<Integer> gids, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("空气站监测数据删除开始：" + gids);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			airInfoMapper.delInfoByGid(gids);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "空气站监测数据删除，GID="+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除空气站监测信息异常" + e);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "空气站监测数据删除异常："+e.getMessage());
		}
		return result;
	}
	
	public Map dataImport(MultipartFile file, HttpServletRequest request) {
		if (logger.isInfoEnabled()) {
			logger.info("导入空气站监测信息-----------------开始");
		}
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		result.put("msg", "成功");
		Workbook wb = null; // 工作区域
		try {
			//获取文件名
	        String filename=file.getOriginalFilename();
	        if (logger.isInfoEnabled()) {
				logger.info("导入空气站监测信息，文件名：" + filename);
			}
	        
	        // 获取文件后缀
	        String prefix=filename.substring(filename.lastIndexOf(".")+1);

	        if (prefix.equals("xlsx")) {
	        	wb = new XSSFWorkbook(file.getInputStream());
	        } else if (prefix.equals("xls")) {
	        	wb = new HSSFWorkbook(file.getInputStream());
	        } else {
	        	result.put("code", -1);
	    		result.put("msg", "不支持的文件类型");
	    		if (logger.isInfoEnabled()) {
	 				logger.info("导入空气站监测信息，不支持的文件类型");
	 			}
	    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(),"导入空气站监测信息，不支持的文件类型");
	    		return result;
	        }
	        
	        Sheet sheet1 = wb.getSheetAt(0);
	        int i = 0;
	        String county = null;
	        String towns = null;
	        String preparer = null;
	        String auditor = null;
	        String paper_time = null;
	        
	        for (Row row : sheet1) {
	        	//解析所属乡镇
	           if (i == 1) {
	        	   county = "刚察县";
	        	   preparer = row.getCell(1).getStringCellValue();
	        	   auditor = row.getCell(3).getStringCellValue();
	        	   paper_time = yyyyMMdd.format(row.getCell(7).getDateCellValue());
	           }
	           if (i >= 3) {
	        	   String city = row.getCell(0).getStringCellValue();
	        	   String station_name = row.getCell(1).getStringCellValue();
	        	   Date quality_time = row.getCell(2).getDateCellValue();
	        	   
	        	   AirInfo airinfo = new AirInfo();
	        	   airinfo.setCounty(county);
	        	   airinfo.setTowns(towns);
	        	   airinfo.setCity(city);
	        	   airinfo.setStation_name(station_name);
	        	   airinfo.setQuality_time(yyyyMMdd.format(quality_time));
	        	   airinfo.setSo2(row.getCell(3).getNumericCellValue());
	        	   airinfo.setNo2((row.getCell(4).getNumericCellValue()));
	        	   airinfo.setCo(row.getCell(5).getNumericCellValue());
	        	   airinfo.setO3_8h(row.getCell(6).getNumericCellValue());
	        	   airinfo.setPm10(row.getCell(7).getNumericCellValue());
	        	   airinfo.setPm2_5(row.getCell(8).getNumericCellValue());
	        	   airinfo.setPreparer(preparer);
	        	   airinfo.setAuditor(auditor);
	        	   airinfo.setPaper_time(paper_time);
	        	   airinfo.setModifier(user.getUser_id());
	        	   
	        	   String gid = airInfoMapper.queryGid(city, station_name, yyyyMMdd.format(quality_time));
	        	   LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, user.getUser_id(), "查询空气站监测，返回GID=" + gid);
	        	   
	        	   try {
		        	   if (StringUtils.isEmpty(gid)) {
		        		   airinfo.setCreator(user.getUser_id());
		        		   airInfoMapper.insertInfo(airinfo);
		        	   } else {
		        		   airinfo.setGid(gid);
		        		   airInfoMapper.updateInfo(airinfo);
		        	   }
	        	   } catch(Exception e) {
        			   if (logger.isErrorEnabled()) {
        					logger.error("空气站监测插入数据失败", e);
        				}
        				result.put("code", -1);
        	    		result.put("msg", "空气站监测插入数据失败");
        	    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "导入空气站监测信息异常："+e.getMessage());
        		   }
	           }
	           i++;
	        }
	        if (logger.isInfoEnabled()) {
 				logger.info("导入空气站监测信息---------------------------成功");
 			}
	        LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, user.getUser_id(), "导入空气站监测信息成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", -1);
    		result.put("msg", "解析文件失败");
    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "导入空气站监测信息异常："+e.getMessage());
		}
		return result;
	}
}
