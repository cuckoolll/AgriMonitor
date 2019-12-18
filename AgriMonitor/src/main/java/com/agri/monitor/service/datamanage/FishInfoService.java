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

import com.agri.monitor.entity.FishInfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.FishInfoMapper;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.vo.FishQueryVO;

@Service
@Transactional
public class FishInfoService {
	
	private static final Logger logger = LoggerFactory.getLogger(FishInfoService.class);
	
	private SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private FishInfoMapper fishInfoMapper;
	 
	public Map queryInfoForPage(FishQueryVO queryVo, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("获取渔业生产信息，入参=date_year:" + queryVo.getDate_year());
		}

		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		result.put("msg", "成功");
		result.put("count", fishInfoMapper.queryInfoCount(queryVo));
		result.put("data", fishInfoMapper.queryInfoForPage(queryVo));
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, 
				"获取渔业生产信息，入参=date_year:" + queryVo.getDate_year());
		return result;
	}
	
	public FishInfo findById(Integer gid, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("获取渔业生产信息，GID=" + gid);
		}
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询渔业生产信息，GID="+gid);
		return fishInfoMapper.findById(gid);
	}
	
	public Map saveOrUpdate(FishInfo fishinfo, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("渔业生产数据更新开始：" + fishinfo);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			fishinfo.setModifier(String.valueOf(userid));
			//更新
			if(!StringUtils.isEmpty(fishinfo.getGid())) {
				fishInfoMapper.updateInfo(fishinfo);
				LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "渔业生产数据更新"+fishinfo);
			}else {//新增
				fishinfo.setCreator(String.valueOf(userid));
				fishInfoMapper.insertInfo(fishinfo);
				LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, userid, "新增渔业生产数据"+fishinfo);
			}
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("保存渔业生产测数据异常" + e);
			LogUtil.log(LogOptTypeEnum.SAVE, LogOptSatusEnum.FAIL, userid, "保存渔业生产数据异常："+e.getMessage());
		}
		return result;
	}
	
	public Map delInfoByGid(List<Integer> gids, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("渔业生产数据删除开始：" + gids);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			fishInfoMapper.delInfoByGid(gids);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "渔业生产数据删除，GID="+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除渔业生产信息异常" + e);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "渔业生产删除异常："+e.getMessage());
		}
		return result;
	}
	
	public Map dataImport(MultipartFile file, HttpServletRequest request) {
		if (logger.isInfoEnabled()) {
			logger.info("导入渔业生产信息-----------------开始");
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
				logger.info("导入渔业生产信息，文件名：" + filename);
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
	 				logger.info("导入渔业生产信息息，不支持的文件类型");
	 			}
	    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(),"导入渔业生产信息，不支持的文件类型");
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
	        	   
	        	   FishInfo fishinfo = new FishInfo();
	        	   fishinfo.setCounty(county);
	        	   fishinfo.setTowns(towns);
	        	   fishinfo.setDate_year(row.getCell(0).getStringCellValue());
	        	   fishinfo.setNaked_carp_count(row.getCell(1).getNumericCellValue());
	        	   fishinfo.setNaked_carp_quality(row.getCell(2).getNumericCellValue());
	        	   fishinfo.setPhytoplankton_density(row.getCell(3).getNumericCellValue());
	        	   fishinfo.setPhytoplankton_avg_density(row.getCell(4).getNumericCellValue());
	        	   fishinfo.setPhytoplankton_count(row.getCell(5).getNumericCellValue());
	        	   fishinfo.setPhytoplankton_avg_count(row.getCell(6).getNumericCellValue());
	        	   fishinfo.setZooplankter_density(row.getCell(7).getNumericCellValue());
	        	   fishinfo.setZooplankter_avg_density(row.getCell(8).getNumericCellValue());
	        	   fishinfo.setZooplankter_count(row.getCell(9).getNumericCellValue());
	        	   fishinfo.setZooplankter_avg_count(row.getCell(10).getNumericCellValue());
	        	   fishinfo.setZoobenthos_density(row.getCell(11).getNumericCellValue());
	        	   fishinfo.setZoobenthos_avg_count(row.getCell(12).getNumericCellValue());
	        	   fishinfo.setZoobenthos_avg_density(row.getCell(13).getNumericCellValue());
	        	   fishinfo.setNaked_carp_seed_in_bhh(row.getCell(14).getNumericCellValue());
	        	   fishinfo.setNaked_carp_seed_in_slh(row.getCell(15).getNumericCellValue());
	        	   fishinfo.setNaked_carp_seed_in_qjh(row.getCell(16).getNumericCellValue());
	        	   fishinfo.setNaked_carp_seed_in_hmh(row.getCell(17).getNumericCellValue());
	        	   fishinfo.setNaked_carp_seed_in_hegh(row.getCell(18).getNumericCellValue());
	        	   fishinfo.setScyzmj(row.getCell(19).getNumericCellValue());
	        	   fishinfo.setScbzhyzmj(row.getCell(20).getNumericCellValue());
	        	   
	        	   fishinfo.setCreator(String.valueOf(user.getUser_id()));
	        	   fishinfo.setModifier(String.valueOf(user.getUser_id()));
	        	   
	        	   String date_year = fishinfo.getDate_year();
	        	   
	        	   try {
	        		   String gid = fishInfoMapper.queryGid(county, date_year);
		        	   if (StringUtils.isEmpty(gid)) {
		        		   fishInfoMapper.insertInfo(fishinfo);
		        	   } else {
		        		   fishinfo.setGid(gid);
		        		   fishInfoMapper.updateInfo(fishinfo);
		        	   }
	        	   } catch(Exception e) {
        			   if (logger.isErrorEnabled()) {
        					logger.error("渔业生产信息插入数据失败", e);
        				}
        				result.put("code", -1);
        	    		result.put("msg", "渔业生产信息插入数据失败");
        	    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "导入渔业生产信息异常："+e.getMessage());
        		   }
	           }
	           i++;
	        }
	        LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, user.getUser_id(), "导入渔业生产信息成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", -1);
    		result.put("msg", "解析文件失败");
    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "导入渔业生产信息异常："+e.getMessage());
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
