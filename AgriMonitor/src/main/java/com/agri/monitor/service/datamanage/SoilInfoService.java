package com.agri.monitor.service.datamanage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
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

import com.agri.monitor.entity.SoilInfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.enums.SoilIndexEnum;
import com.agri.monitor.mapper.SoilInfoMapper;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.utils.UrbanAreaUtil;
import com.agri.monitor.vo.SoilQueryVO;

@Service
@Transactional
public class SoilInfoService {
	private static final Logger logger = LoggerFactory.getLogger(SoilInfoService.class);
	
	private SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired 
	private SoilInfoMapper soilInfoMapper;
	
	public Map queryInfoForPage(SoilQueryVO queryVo, String userid) { 
		if (logger.isInfoEnabled()) {
			logger.info("获取土壤监测信息，入参=date_year:" + queryVo.getDate_year()); 
		}
	
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0); 
		result.put("msg", "成功");
		result.put("count", soilInfoMapper.queryInfoCount(queryVo));
		result.put("data", soilInfoMapper.queryInfoForPage(queryVo));
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "获取土壤监测信息，入参=date_year:" + queryVo.getDate_year()); 
		return result; 
	}
	
	public SoilInfo findById(Integer gid, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("获取土壤监测信息，GID=" + gid); 
		}
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询土壤监测信息，GID="+gid); 
		return soilInfoMapper.findById(gid); 
	}
	
	public Map saveOrUpdate(SoilInfo soilinfo, String userid) { 
		if (logger.isInfoEnabled()) { 
			logger.info("土壤监测数据更新开始：" + soilinfo); 
		} 
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0); 
		
		try { 
			soilinfo.setModifier(String.valueOf(userid));
			//更新 
			if(!StringUtils.isEmpty(soilinfo.getGid())) {
				soilInfoMapper.updateInfo(soilinfo); 
				LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "土壤监测数据更新" + soilinfo); 
			} else {
				//新增
				soilinfo.setCreator(String.valueOf(userid));
				soilInfoMapper.insertInfo(soilinfo); 
				LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, userid, "新增土壤监测数据" + soilinfo); 
			} 
		} catch (Exception e) {
			result.put("code", -1); logger.error("保存土壤监测数据异常" + e);
			LogUtil.log(LogOptTypeEnum.SAVE, LogOptSatusEnum.FAIL, userid, "保存土壤监测数据异常：" + e.getMessage()); 
		}
		return result;
	}
	
	public Map delInfoByGid(List<Integer> gids, String userid) { 
		if (logger.isInfoEnabled()) {
			logger.info("土壤监测数据删除开始：" + gids); 
		} 
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0); 
		
		try {
			soilInfoMapper.delInfoByGid(gids);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "土壤监测数据删除，GID="+gids); 
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除土壤监测信息异常" + e); 
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "土壤监测数据删除异常："+ e.getMessage()); 
		}
		return result;
	}
	
	public Map dataImport(MultipartFile file, HttpServletRequest request) { 
		if (logger.isInfoEnabled()) {
			logger.info("导入土壤监测信息-----------------开始");
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
				logger.info("导入土壤监测信息，文件名：" + filename); 
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
					logger.info("导入土壤监测信息，不支持的文件类型"); 
				}
				LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(),"导入土壤监测数据，不支持的文件类型"); 
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
//					towns = row.getCell(1).getStringCellValue(); 
//					 if (!UrbanAreaUtil.isLegalTown(towns)) {
//		        		   result.put("code", -1);
//		        		   result.put("msg", "报表乡镇填写错误，请重新选择所属乡镇");
//		        		   return result;
//		        	   }
				} 
				if (i >= 2) {
					final Cell cell0 = row.getCell(0);
	        	    if (cell0 == null || StringUtils.isEmpty(cell0.toString())) {
	        	    	break;
	        	    }
					
					SoilInfo soilinfo = new SoilInfo(); 
					soilinfo.setCounty(county);
					soilinfo.setTowns(towns);	
					
					int year = (int) row.getCell(0).getNumericCellValue();
					soilinfo.setDate_year((int) row.getCell(0).getNumericCellValue());
					soilinfo.setCode_number(row.getCell(1).getStringCellValue());
					soilinfo.setOrganic(row.getCell(2).getNumericCellValue());
					soilinfo.setNitrogen(row.getCell(3).getNumericCellValue());
					soilinfo.setPhosphorus(row.getCell(4).getNumericCellValue());
					soilinfo.setEffective_phosphorus(row.getCell(5).getNumericCellValue());
					soilinfo.setPotassium(row.getCell(6).getNumericCellValue());
					soilinfo.setPh(row.getCell(7).getNumericCellValue());
					soilinfo.setSalinity(row.getCell(8).getNumericCellValue());
					soilinfo.setAvailable_potassium(row.getCell(9).getNumericCellValue());
					soilinfo.setSlow_release_potassium(row.getCell(10).getNumericCellValue());
					soilinfo.setCreator(String.valueOf(user.getUser_id()));
					soilinfo.setModifier(String.valueOf(user.getUser_id()));
	
					int date_year = soilinfo.getDate_year();
					String code_number = soilinfo.getCode_number();
					
					try { 
						String gid = soilInfoMapper.queryGid(String.valueOf(date_year), code_number);
						
						if (StringUtils.isEmpty(gid)) {
							soilInfoMapper.insertInfo(soilinfo); 
						} else { 
							soilinfo.setGid(gid); 
							soilInfoMapper.updateInfo(soilinfo); 
						}
					} catch(Exception e) {
						if (logger.isErrorEnabled()) {
							logger.error("土壤监测插入数据失败", e); 
						} 
						result.put("code", -1);
						result.put("msg", "土壤监测插入数据失败"); 
						LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "导入土壤监测信息异常："+e.getMessage()); 
					} 
				} 
					i++; 
			}
			LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, user.getUser_id(), "导入土壤监测信息成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", -1);
			result.put("msg", "解析文件失败");
			LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "导入土壤监测信息异常："+e.getMessage()); 
		} 
		return result;
	}
	
	public List<Map> getSoilIndex() {
		List<Map> result = new ArrayList();
		for (SoilIndexEnum soilIndex : SoilIndexEnum.values()) {
			Map map = new HashMap();
			map.put("id", soilIndex.getId());
			map.put("text", soilIndex.getText());
			result.add(map);
		}
		return result;
	}
	
	public Map queryAnalysisData(HttpServletRequest request) {
		Map param = new HashMap();
		param.put("date_year", request.getParameter("date_year"));
		param.put("soilindex", request.getParameter("soilindex"));
		if (logger.isInfoEnabled()) {
			logger.info("土壤数据分析查询：" + param);
		}
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		String userid = user.getUser_id();
		
		List<Map> analysisData = new ArrayList();
		try {
			analysisData = soilInfoMapper.queryAnalysisData(param);
			LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "土壤数据分析查询：" + param);
		} catch (Exception e) {
			logger.error("土壤数据分析查询失败", e);
			LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.FAIL, userid, "土壤数据分析查询失败" + e.getMessage());
		}
		
		final String soilindex = (String) param.get("soilindex");
		
		final List dataList = new ArrayList();
		final List code_numberList = new ArrayList();
		
		for (Map map : analysisData) {
			dataList.add(map.get(soilindex));
			code_numberList.add(map.get("code_number"));
		}
		
		final Map result = new HashMap();
		result.put("data", dataList);
		result.put("code_number", code_numberList);
		result.put("gridData", analysisData);
		
		return result;
	}
}
