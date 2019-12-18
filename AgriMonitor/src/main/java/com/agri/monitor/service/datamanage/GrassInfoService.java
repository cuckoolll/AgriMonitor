package com.agri.monitor.service.datamanage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
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

import com.agri.monitor.entity.GrassInfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.GrassIndexEnum;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.GrassInfoMapper;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.vo.GrassQueryVO;

@Service
@Transactional
public class GrassInfoService {
	
	private static final Logger logger = LoggerFactory.getLogger(GrassInfoService.class);
	
	private SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private GrassInfoMapper grassInfoMapper;
	 
	public Map queryInfoForPage(GrassQueryVO queryVo, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("获取草地生态监测信息，入参=date_year:" + queryVo.getDate_year());
		}

		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		result.put("msg", "成功");
		result.put("count", grassInfoMapper.queryInfoCount(queryVo));
		result.put("data", grassInfoMapper.queryInfoForPage(queryVo));
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, 
				"获取草地生态监测信息，入参=date_year:" + queryVo.getDate_year());
		return result;
	}
	
	public GrassInfo findById(Integer gid, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("获取草地生态监测信息，GID=" + gid);
		}
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询草地生态监测信息，GID="+gid);
		return grassInfoMapper.findById(gid);
	}
	
	public Map saveOrUpdate(GrassInfo grassinfo, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("草地生态监测数据更新开始：" + grassinfo);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			grassinfo.setModifier(String.valueOf(userid));
			//更新
			if(!StringUtils.isEmpty(grassinfo.getGid())) {
				grassInfoMapper.updateInfo(grassinfo);
				LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "草地生态监测数据更新"+grassinfo);
			}else {//新增
				grassinfo.setCreator(String.valueOf(userid));
				grassInfoMapper.insertInfo(grassinfo);
				LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, userid, "新增草地生态监测数据"+grassinfo);
			}
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("保存草地生态监测数据异常" + e);
			LogUtil.log(LogOptTypeEnum.SAVE, LogOptSatusEnum.FAIL, userid, "保存草地生态监测数据异常："+e.getMessage());
		}
		return result;
	}
	
	public Map delInfoByGid(List<Integer> gids, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("草地生态监测数据删除开始：" + gids);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			grassInfoMapper.delInfoByGid(gids);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "草地生态监测数据删除，GID="+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除草地生态监测信息异常" + e);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "草地生态监测数据删除异常："+e.getMessage());
		}
		return result;
	}
	
	public Map dataImport(MultipartFile file, HttpServletRequest request) {
		if (logger.isInfoEnabled()) {
			logger.info("导入草地生态监测信息-----------------开始");
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
				logger.info("导入草地生态监测信息，文件名：" + filename);
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
	 				logger.info("导入草地生态监测信息，不支持的文件类型");
	 			}
	    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(),"导入草地生态监测数据，不支持的文件类型");
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
//	        	   towns = row.getCell(1).getStringCellValue();
//	        	   if (!UrbanAreaUtil.isLegalTown(towns)) {
//	        		   result.put("code", -1);
//	        		   result.put("msg", "报表乡镇填写错误，请重新选择所属乡镇");
//	        		   return result;
//	        	   }
	           }
	           if (i >= 3) {
	        	   final Cell cell0 = row.getCell(0);
	        	   if (cell0 == null || StringUtils.isEmpty(cell0.toString())) {
	        		   break;
	        	   }
	        	   
	        	   GrassInfo grassinfo = new GrassInfo();
	        	   grassinfo.setCounty(county);
	        	   grassinfo.setTowns(towns);
	        	   grassinfo.setDate_year(row.getCell(0).getStringCellValue());
	        	   grassinfo.setGrass_area(row.getCell(1).getNumericCellValue());
	        	   grassinfo.setGrass_usable_area(row.getCell(2).getNumericCellValue());
	        	   grassinfo.setGrass_retain_area(row.getCell(3).getNumericCellValue());
	        	   grassinfo.setGrass_unforage(row.getCell(4).getNumericCellValue());
	        	   grassinfo.setGrass_animal_balance(row.getCell(5).getNumericCellValue());
	        	   grassinfo.setPlateau_pika_area(row.getCell(6).getNumericCellValue());
	        	   grassinfo.setPlateau_zokor_area(row.getCell(7).getNumericCellValue());
	        	   grassinfo.setGrass_worm_area(row.getCell(8).getNumericCellValue());
	        	   grassinfo.setGrasshopper_area(row.getCell(9).getNumericCellValue());
	        	   grassinfo.setCreator(String.valueOf(user.getUser_id()));
	        	   grassinfo.setModifier(String.valueOf(user.getUser_id()));
	        	   
	        	   String date_year = grassinfo.getDate_year();
	        	   
	        	   try {
	        		   String gid = grassInfoMapper.queryGid(county, date_year);
		        	   if (StringUtils.isEmpty(gid)) {
		        		   grassInfoMapper.insertInfo(grassinfo);
		        	   } else {
		        		   grassinfo.setGid(gid);
		        		   grassInfoMapper.updateInfo(grassinfo);
		        	   }
	        	   } catch(Exception e) {
        			   if (logger.isErrorEnabled()) {
        					logger.error("草地生态监测插入数据失败", e);
        				}
        				result.put("code", -1);
        	    		result.put("msg", "草地生态监测插入数据失败");
        	    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "导入草地生态监测信息异常："+e.getMessage());
        		   }
	           }
	           i++;
	        }
	        LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, user.getUser_id(), "导入草地生态监测信息成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", -1);
    		result.put("msg", "解析文件失败");
    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "导入草地生态监测信息异常："+e.getMessage());
		}
		return result;
	}
	
	public List<Map> getGrassIndex() {
		List<Map> result = new ArrayList();
		for (GrassIndexEnum grassIndex : GrassIndexEnum.values()) {
			Map map = new HashMap();
			map.put("id", grassIndex.getId());
			map.put("text", grassIndex.getText());
			result.add(map);
		}
		return result;
	}
	
	public Map queryAnalysisData(HttpServletRequest request) {
		Map param = new HashMap();
		param.put("grassIndex", request.getParameter("grassindex"));
		if (logger.isInfoEnabled()) {
			logger.info("草地生态数据分析查询：" + param);
		}
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		String userid = user.getUser_id();
		
		List<Map> analysisData = new ArrayList();
		try {
			analysisData = grassInfoMapper.queryAnalysisData(param);
			LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "草地生态数据分析查询：" + param);
		} catch (Exception e) {
			logger.error("草地生态数据分析查询失败", e);
			LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.FAIL, userid, "草地生态数据分析查询失败" + e.getMessage());
		}
		
		final String grassIndex = (String) param.get("grassIndex");
		
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		
		Object[] datas = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
		
		for (int i = 0; i < analysisData.size(); i++) {
			int date_year = (Integer) analysisData.get(i).get("date_year");
			Object obj = analysisData.get(i).get(grassIndex);
			if (obj instanceof java.lang.Double) {
				datas[year - date_year] = (Double) obj;
			} else if (obj instanceof java.lang.Integer) {
				datas[year - date_year] = (Integer) obj;
			}
			
		}
		
		final List dataList = Arrays.asList(datas);
		Collections.reverse(dataList);
		
		for (int i = year; i > year - 10; i--) {
			boolean exist = false;
			for (Map map : analysisData) {
				if (map.get("date_year") != null) {
					int tempYear = (Integer) map.get("date_year");
					if (tempYear == i) {
						exist = true;
						continue;
					}
				}
			}
			if (!exist) {
				Map o = new HashMap();
				o.put("date_year", i);
				o.put(grassIndex, 0);
				analysisData.add(o);
			}
		}
		
		final Map result = new HashMap();
		result.put("data", dataList);
		result.put("gridData", analysisData);
//		result.put("towns", townsList);
//		result.put("date_year", date_yearList);
		
		return result;
	}
}
