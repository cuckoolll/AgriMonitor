package com.agri.monitor.service.datamanage;

import java.text.SimpleDateFormat;
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

import com.agri.monitor.entity.FacilityConditionInfo;
import com.agri.monitor.entity.GrassInfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.FacilitiesConditionInfoMapper;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.vo.FacilityConditionQueryVO;

@Service
@Transactional
public class FacilitiesConditionInfoService {
	
	private static final Logger logger = LoggerFactory.getLogger(FacilitiesConditionInfoService.class);
	
	private SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private FacilitiesConditionInfoMapper facilitiesConditionInfoMapper;
	 
	public Map queryInfoForPage(FacilityConditionQueryVO queryVo, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("获取农业装备信息，入参=date_year:" + queryVo.getDate_year() 
										+ ",date_year1:" + queryVo.getDate_year1()
										+ ",agri_address:" + queryVo.getAgri_address());
		}

		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		result.put("msg", "成功");
		result.put("count", facilitiesConditionInfoMapper.queryInfoCount(queryVo));
		result.put("data", facilitiesConditionInfoMapper.queryInfoForPage(queryVo));
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, 
				"获取农业装备信息，入参=date_year:" + queryVo.getDate_year()  
									+ ",date_year1:" + queryVo.getDate_year1()
									+ ",agri_address:" + queryVo.getAgri_address());
		return result;
	}
	
	public FacilityConditionInfo findById(Integer gid, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("获取农业装备信息，GID=" + gid);
		}
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询农业装备信息，GID="+gid);
		return facilitiesConditionInfoMapper.findById(gid);
	}
	
	public Map saveOrUpdate(FacilityConditionInfo facilityconditioninfo, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("农业装备信息更新开始：" + facilityconditioninfo);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			facilityconditioninfo.setModifier(String.valueOf(userid));
			//更新
			if(!StringUtils.isEmpty(facilityconditioninfo.getGid())) {
				facilitiesConditionInfoMapper.updateInfo(facilityconditioninfo);
				LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "农业装备信息更新"+facilityconditioninfo);
			}else {//新增
				facilityconditioninfo.setCreator(String.valueOf(userid));
				facilitiesConditionInfoMapper.insertInfo(facilityconditioninfo);
				LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, userid, "农业装备信息数据"+facilityconditioninfo);
			}
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("保存农业装备信息异常" + e);
			LogUtil.log(LogOptTypeEnum.SAVE, LogOptSatusEnum.FAIL, userid, "保存农业装备信息异常："+e.getMessage());
		}
		return result;
	}
	
	public Map delInfoByGid(List<Integer> gids, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("农业装备信息删除开始：" + gids);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			facilitiesConditionInfoMapper.delInfoByGid(gids);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "农业装备信息删除，GID="+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除农业装备信息异常" + e);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "农业装备信息删除异常："+e.getMessage());
		}
		return result;
	}
	
	public Map dataImport(MultipartFile file, HttpServletRequest request) {
		if (logger.isInfoEnabled()) {
			logger.info("导入农业装备信息-----------------开始");
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
				logger.info("导入农业装备信息，文件名：" + filename);
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
	 				logger.info("导入农业装备信息，不支持的文件类型");
	 			}
	    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(),"导入农业装备数据，不支持的文件类型");
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
	           }
	           if (i >= 3) {
	        	   final Cell cell0 = row.getCell(0);
	        	   if (cell0 == null) {
	        		   break;
	        	   }
	        	   
	        	   FacilityConditionInfo facilityconditioninfo = new FacilityConditionInfo();
	        	   facilityconditioninfo.setCounty(county);
	        	   facilityconditioninfo.setTowns(towns);
	        	   facilityconditioninfo.setDate_year(row.getCell(0).getStringCellValue());
	        	   facilityconditioninfo.setAgri_address(row.getCell(1).getStringCellValue());
	        	   facilityconditioninfo.setLarge_medium_tractors(row.getCell(2).getNumericCellValue());
	        	   facilityconditioninfo.setSmall_walking_tractors(row.getCell(3).getNumericCellValue());
	        	   facilityconditioninfo.setCombine_harvester(row.getCell(4).getNumericCellValue());
	        	   facilityconditioninfo.setMotor_thresher(row.getCell(5).getNumericCellValue());
	        	   facilityconditioninfo.setAgricultural_carriage_car(row.getCell(6).getNumericCellValue());
	        	   facilityconditioninfo.setRural_hydropower_stations(row.getCell(7).getNumericCellValue());
	        	   facilityconditioninfo.setGenerating_capacity(row.getCell(8).getNumericCellValue());
	        	   facilityconditioninfo.setElectric_energy(row.getCell(9).getNumericCellValue());
	        	   facilityconditioninfo.setRural_power_consumption(row.getCell(10).getNumericCellValue());
	        	   facilityconditioninfo.setEffective_irrigation_area(row.getCell(11).getNumericCellValue());
	        	   facilityconditioninfo.setWaterlogging_drought_area(row.getCell(12).getNumericCellValue());
	        	   facilityconditioninfo.setMechanical_irrigation_area(row.getCell(13).getNumericCellValue());
	        	   facilityconditioninfo.setReservoir(row.getCell(14).getNumericCellValue());
	        	   facilityconditioninfo.setChannel_area(row.getCell(15).getNumericCellValue());
	        	   facilityconditioninfo.setRemark(row.getCell(16).getStringCellValue());
	        	   
	        	   facilityconditioninfo.setCreator(String.valueOf(user.getUser_id()));
	        	   facilityconditioninfo.setModifier(String.valueOf(user.getUser_id()));
	        	   
	        	   String date_year = facilityconditioninfo.getDate_year();
	        	   
	        	   try {
	        		   String gid = facilitiesConditionInfoMapper.queryGid(county, date_year);
		        	   if (StringUtils.isEmpty(gid)) {
		        		   facilitiesConditionInfoMapper.insertInfo(facilityconditioninfo);
		        	   } else {
		        		   facilityconditioninfo.setGid(gid);
		        		   facilitiesConditionInfoMapper.updateInfo(facilityconditioninfo);
		        	   }
	        	   } catch(Exception e) {
        			   if (logger.isErrorEnabled()) {
        					logger.error("农业装备插入数据失败", e);
        				}
        				result.put("code", -1);
        	    		result.put("msg", "农业装备插入数据失败");
        	    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "导入农业装备信息异常："+e.getMessage());
        		   }
	           }
	           i++;
	        }
	        LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, user.getUser_id(), "导入农业装备信息成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", -1);
    		result.put("msg", "解析文件失败");
    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "导入农业装备信息异常："+e.getMessage());
		}
		return result;
	}
	
//	public List<Map> getGrassIndex() {
//		List<Map> result = new ArrayList();
//		for (GrassIndexEnum grassIndex : GrassIndexEnum.values()) {
//			Map map = new HashMap();
//			map.put("id", grassIndex.getId());
//			map.put("text", grassIndex.getText());
//			result.add(map);
//		}
//		return result;
//	}
//	
//	public Map queryAnalysisData(HttpServletRequest request) {
//		Map param = new HashMap();
//		param.put("grassIndex", request.getParameter("grassindex"));
//		if (logger.isInfoEnabled()) {
//			logger.info("草地生态数据分析查询：" + param);
//		}
//		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
//		String userid = user.getUser_id();
//		
//		List<Map> analysisData = new ArrayList();
//		try {
//			analysisData = grassInfoMapper.queryAnalysisData(param);
//			LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "草地生态数据分析查询：" + param);
//		} catch (Exception e) {
//			logger.error("草地生态数据分析查询失败", e);
//			LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.FAIL, userid, "草地生态数据分析查询失败" + e.getMessage());
//		}
//		
//		final String grassIndex = (String) param.get("grassIndex");
//		
//		Calendar c = Calendar.getInstance();
//		int year = c.get(Calendar.YEAR);
//		
//		Object[] datas = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
//		
//		for (int i = 0; i < analysisData.size(); i++) {
//			int date_year = (Integer) analysisData.get(i).get("date_year");
//			Object obj = analysisData.get(i).get(grassIndex);
//			if (obj instanceof java.lang.Double) {
//				datas[year - date_year] = (Double) obj;
//			} else if (obj instanceof java.lang.Integer) {
//				datas[year - date_year] = (Integer) obj;
//			}
//			
//		}
//		
//		final List dataList = Arrays.asList(datas);
//		Collections.reverse(dataList);
//		
//		for (int i = year; i > year - 10; i--) {
//			boolean exist = false;
//			for (Map map : analysisData) {
//				if (map.get("date_year") != null) {
//					int tempYear = (Integer) map.get("date_year");
//					if (tempYear == i) {
//						exist = true;
//						continue;
//					}
//				}
//			}
//			if (!exist) {
//				Map o = new HashMap();
//				o.put("date_year", i);
//				o.put(grassIndex, 0);
//				analysisData.add(o);
//			}
//		}
//		
//		final Map result = new HashMap();
//		result.put("data", dataList);
//		result.put("gridData", analysisData);
////		result.put("towns", townsList);
////		result.put("date_year", date_yearList);
//		
//		return result;
//	}
}
