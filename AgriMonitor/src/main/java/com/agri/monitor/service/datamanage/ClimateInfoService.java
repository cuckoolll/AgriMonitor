package com.agri.monitor.service.datamanage;

import java.text.SimpleDateFormat;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.agri.monitor.ApplicationRunnerImpl;
import com.agri.monitor.entity.ClimateInfo;
import com.agri.monitor.entity.FarmInfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.entity.WaterInfo;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.ClimateInfoMapper;
import com.agri.monitor.mapper.WaterInfoMapper;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.vo.ClimateQueryVO;
import com.agri.monitor.vo.WaterQueryVO;

@Service
@Transactional
public class ClimateInfoService {
	
	private static final Logger logger = LoggerFactory.getLogger(ClimateInfoService.class);
	
	private SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private ClimateInfoMapper climateInfoMapper;
	 
	public Map queryInfoForPage(ClimateQueryVO queryVo, Integer userid) {
		if (logger.isInfoEnabled()) {
			logger.info("获取气候监测信息，入参=date_year:" + queryVo.getDate_year());
		}

		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		result.put("msg", "成功");
		result.put("count", climateInfoMapper.queryInfoCount(queryVo));
		result.put("data", climateInfoMapper.queryInfoForPage(queryVo));
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, 
				"获取气候监测信息，入参=date_year:" + queryVo.getDate_year());
		return result;
	}
	
	public ClimateInfo findById(Integer gid, Integer userid) {
		if (logger.isInfoEnabled()) {
			logger.info("获取气候监测信息，GID=" + gid);
		}
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询气候监测信息，GID="+gid);
		return climateInfoMapper.findById(gid);
	}
	
	public Map saveOrUpdate(ClimateInfo climateinfo, Integer userid) {
		if (logger.isInfoEnabled()) {
			logger.info("气候监测数据更新开始：" + climateinfo);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			climateinfo.setModifier(String.valueOf(userid));
			//更新
			if(!StringUtils.isEmpty(climateinfo.getGid())) {
				climateInfoMapper.updateInfo(climateinfo);
				LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "气候监测数据更新"+climateinfo);
			}else {//新增
				climateinfo.setCreator(String.valueOf(userid));
				climateInfoMapper.insertInfo(climateinfo);
				LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, userid, "新增气候监测数据"+climateinfo);
			}
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("保存气候监测数据异常" + e);
			LogUtil.log(LogOptTypeEnum.SAVE, LogOptSatusEnum.FAIL, userid, "保存气候监测数据异常："+e.getMessage());
		}
		return result;
	}
	
	public Map delInfoByGid(List<Integer> gids, Integer userid) {
		if (logger.isInfoEnabled()) {
			logger.info("气候监测数据删除开始：" + gids);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			climateInfoMapper.delInfoByGid(gids);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "气候监测数据删除，GID="+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除气候监测信息异常" + e);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "气候监测数据删除异常："+e.getMessage());
		}
		return result;
	}
	
	public Map dataImport(MultipartFile file, HttpServletRequest request) {
		if (logger.isInfoEnabled()) {
			logger.info("导入气候监测信息-----------------开始");
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
				logger.info("导入气候监测信息，文件名：" + filename);
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
	 				logger.info("导入气候监测信息，不支持的文件类型");
	 			}
	    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(),"导入气候监测数据，不支持的文件类型");
	    		return result;
	        }
	        
	        Sheet sheet1 = wb.getSheetAt(0);
	        int i = 0;
	        String county = null;
        	String towns = null;
	        
	        for (Row row : sheet1) {
	        	//解析所属乡镇
	           if (i == 1) {
	        	   county = "刚察县";
	        	   towns = row.getCell(1).getStringCellValue();
	        	   //TODO 与缓存中乡镇信息对比
	           }
	           if (i >= 3) {
	        	   ClimateInfo climateinfo = new ClimateInfo();
	        	   climateinfo.setCounty(county);
	        	   climateinfo.setTowns(towns);
	        	   climateinfo.setDate_year(row.getCell(0).getStringCellValue());
	        	   climateinfo.setYear_avg_temperature(row.getCell(1).getNumericCellValue());
	        	   climateinfo.setHigh_temperature(row.getCell(2).getNumericCellValue());
	        	   climateinfo.setLow_temperature(row.getCell(3).getNumericCellValue());
	        	   climateinfo.setYear_precipitation(row.getCell(4).getNumericCellValue());
	        	   climateinfo.setMouth_high_precipitation(row.getCell(5).getNumericCellValue());
	        	   climateinfo.setDay_high_precipitation(row.getCell(6).getNumericCellValue());
	        	   climateinfo.setYear_avg_winds(row.getCell(7).getNumericCellValue());
	        	   climateinfo.setHigh_winds(row.getCell(8).getNumericCellValue());
	        	   climateinfo.setYear_high_winds_days((int) row.getCell(9).getNumericCellValue());
	        	   climateinfo.setYear_avg_pressure(row.getCell(10).getNumericCellValue());
	        	   climateinfo.setYear_thunderstorm_days((int)row.getCell(11).getNumericCellValue());
	        	   climateinfo.setYear_sandstorm_days((int)row.getCell(12).getNumericCellValue());
	        	   climateinfo.setCreator(String.valueOf(user.getUser_id()));
	        	   climateinfo.setModifier(String.valueOf(user.getUser_id()));
	        	   
	        	   String date_year = climateinfo.getDate_year();
	        	   
	        	   try {
	        		   String gid = climateInfoMapper.queryGid(county, towns, date_year);
		        	   if (StringUtils.isEmpty(gid)) {
		        		   climateInfoMapper.insertInfo(climateinfo);
		        	   } else {
		        		   climateinfo.setGid(gid);
		        		   climateInfoMapper.updateInfo(climateinfo);
		        	   }
	        	   } catch(Exception e) {
        			   if (logger.isErrorEnabled()) {
        					logger.error("气候监测插入数据失败", e);
        				}
        				result.put("code", -1);
        	    		result.put("msg", "气候监测插入数据失败");
        	    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "导入气候监测信息异常："+e.getMessage());
        		   }
	           }
	           i++;
	        }
	        LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, user.getUser_id(), "导入气候监测信息成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", -1);
    		result.put("msg", "解析文件失败");
    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "导入气候监测信息异常："+e.getMessage());
		}
		return result;
	}
}
