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
import com.agri.monitor.entity.FarmInfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.entity.WaterInfo;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.WaterInfoMapper;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.utils.UrbanAreaUtil;
import com.agri.monitor.vo.WaterQueryVO;

@Service
@Transactional
public class WaterInfoService {
	
	private static final Logger logger = LoggerFactory.getLogger(WaterInfoService.class);
	
	private SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
	
	@Autowired
	private WaterInfoMapper waterInfoMapper;
	 
	public List<Map> queryQualityType(WaterQueryVO queryVo, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("获取水质监测信息表头，入参=quality_address:" + queryVo.getQuality_address() 
				+ ", quality_time:" + queryVo.getQuality_time());
		}
		List<Map> data = new ArrayList();
		
		try {
			final List<String> quailtyTypeList = waterInfoMapper.queryQualityType(queryVo);
			int i = 0;
			for (String quailtyType : quailtyTypeList) {
				final Map colsMap = new HashMap();			//表头字典
				colsMap.put("field", "param" + String.valueOf(i));
				colsMap.put("title", quailtyType);
				colsMap.put("width", "100");
				data.add(colsMap);
				i++;
			}
		} catch (Exception e) {
			logger.error("获取水质监测信息表头异常" + e);
			LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.FAIL, userid, 
					"获取水质监测信息表头，入参=quality_address:" + queryVo.getQuality_address() + ", quality_time:" + queryVo.getQuality_time());
		}
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, 
				"获取水质监测信息表头，入参=quality_address:" + queryVo.getQuality_address() + ", quality_time:" + queryVo.getQuality_time());
		return data;
	}
	
	/**
	 * 按行查询水质监测信息
	 * @param queryVo
	 * @param userid
	 * @return
	 */
	public Map queryWaterInfoOnLine(WaterQueryVO queryVo, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("获取水质监测项目对比数据，入参=quality_address:" + queryVo.getQuality_address() 
				+ ", quality_time:" + queryVo.getQuality_time());
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		result.put("msg", "成功");
//		result.put("count", waterInfoMapper.queryInfoCount(queryVo));
		
		final List<Map> data = new ArrayList();
		try {
			final List<String> quailtyTypeList = waterInfoMapper.queryQualityType(queryVo);
			
			final Map colsMap = new HashMap();			//表头字典
			int i = 0;
			for (String quailtyType : quailtyTypeList) {
				colsMap.put(quailtyType, "param" + String.valueOf(i));
				i++;
			} 
			final List<Map> queryInfo = waterInfoMapper.queryInfoByCountryAndTime(queryVo);
			for (Map info : queryInfo) {
				final Map tempMap = new HashMap();
				tempMap.put("address_time", info.get("quality_address") + "_" + info.get("quality_time"));
				if (!data.contains(tempMap)) {
					data.add(tempMap);
				}
			}
			
			//列转行
			for (Map info : queryInfo) {
				for (Map map : data) {
					if ((info.get("quality_address") + "_" + info.get("quality_time")).equals((String) map.get("address_time"))) {
						map.put(colsMap.get(info.get("quality_type")), info.get("quality_result"));
						map.put("quality_address", info.get("quality_address"));
						map.put("quality_time", info.get("quality_time"));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", -1);
			result.put("msg", "获取水质监测项目对比数据异常");
			logger.error("获取水质监测项目对比数据异常" + e);
			LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.FAIL, userid, 
					"获取水质监测项目对比数据，入参=quality_address:" + queryVo.getQuality_address() + ", quality_time:" + queryVo.getQuality_time());
		}
		
		result.put("data", data);
		
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, 
				"获取水质监测项目对比数据，入参=quality_address:" + queryVo.getQuality_address() + ", quality_time:" + queryVo.getQuality_time());
		return result;
	}
	
	/**
	 * 按列查询水质监测数据 .
	 * @param queryVo .
	 * @param userid .
	 * @return .
	 */
	public Map queryInfoByCountryAndTimeForPage(WaterQueryVO queryVo, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("获取水质监测信息，入参=quality_address:" + queryVo.getQuality_address() 
				+ ", quality_time:" + queryVo.getQuality_time() + ",quality_time1:" + queryVo.getQuality_time1());
		}

		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		result.put("msg", "成功");
		result.put("count", waterInfoMapper.queryInfoCount(queryVo));
		result.put("data", waterInfoMapper.queryInfoByCountryAndTimeForPage(queryVo));
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, 
				"获取水质监测信息，入参=quality_address:" + queryVo.getQuality_address() + ", quality_time:" + queryVo.getQuality_time()
				+ ",quality_time1:" + queryVo.getQuality_time1());
		return result;
	}
	
	public WaterInfo findById(Integer gid, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("获取水质监测信息，GID=" + gid);
		}
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询水质监测信息，GID="+gid);
		return waterInfoMapper.findById(gid);
	}
	
	public Map saveOrUpdate(WaterInfo waterinfo, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("水质监测数据更新开始：" + waterinfo);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			waterinfo.setModifier(String.valueOf(userid));
			//更新
			if(!StringUtils.isEmpty(waterinfo.getGid())) {
				waterInfoMapper.updateWaterInfo(waterinfo);
				LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "水质监测数据更新"+waterinfo);
			}else {//新增
				waterinfo.setCreator(String.valueOf(userid));
				waterInfoMapper.insertWaterInfo(waterinfo);
				LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, userid, "新增水质监测数据"+waterinfo);
			}
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("保存水质监测数据异常" + e);
			LogUtil.log(LogOptTypeEnum.SAVE, LogOptSatusEnum.FAIL, userid, "保存水质监测数据异常："+e.getMessage());
		}
		return result;
	}
	
	public Map delInfoByGid(List<Integer> gids, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("水质监测数据删除开始：" + gids);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			waterInfoMapper.delInfoByGid(gids);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "水质监测数据删除，GID="+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除水质监测信息异常" + e);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "水质监测数据删除异常："+e.getMessage());
		}
		return result;
	}
	
	public Map dataImport(MultipartFile file, HttpServletRequest request) {
		if (logger.isInfoEnabled()) {
			logger.info("导入水质监测信息-----------------开始");
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
				logger.info("导入水质监测信息，文件名：" + filename);
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
	 				logger.info("导入水质监测信息，不支持的文件类型");
	 			}
	    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(),"导入水质监测信息，不支持的文件类型");
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
	           if (i >= 2) {
	        	   String quality_address = row.getCell(0).getStringCellValue();
	        	   String quality_time = yyyyMMdd.format(row.getCell(1).getDateCellValue());
	        	   String quality_type = row.getCell(2).getStringCellValue();
	        	   
	        	   WaterInfo waterinfo = new WaterInfo();
	        	   waterinfo.setCounty(county);
	        	   waterinfo.setTowns(towns);
	        	   waterinfo.setQuality_address(quality_address);
	        	   waterinfo.setQuality_time(quality_time);
	        	   waterinfo.setQuality_type(quality_type);
	        	   waterinfo.setQuality_result(row.getCell(3).getStringCellValue());
	        	   waterinfo.setRemarks(row.getCell(4).getStringCellValue());
	        	   waterinfo.setCreator(String.valueOf(user.getUser_id()));
	        	   waterinfo.setModifier(String.valueOf(user.getUser_id()));
	        	   
	        	   String gid = waterInfoMapper.queryGid(quality_address, quality_time, quality_type);
	        	   LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, user.getUser_id(), "查询水质监测，返回GID=" + gid);
	        	   
	        	   try {
		        	   if (StringUtils.isEmpty(gid)) {
	        			   waterInfoMapper.insertWaterInfo(waterinfo);
		        	   } else {
		        		   waterinfo.setGid(gid);
		        		   waterInfoMapper.updateWaterInfo(waterinfo);
		        	   }
	        	   } catch(Exception e) {
        			   if (logger.isErrorEnabled()) {
        					logger.error("水质监测插入数据失败", e);
        				}
        				result.put("code", -1);
        	    		result.put("msg", "水质监测插入数据失败");
        	    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "导入水质监测信息异常："+e.getMessage());
        		   }
	           }
	           i++;
	        }
	        if (logger.isInfoEnabled()) {
 				logger.info("导入水质监测信息---------------------------成功");
 			}
	        LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, user.getUser_id(), "导入水质监测信息成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", -1);
    		result.put("msg", "解析文件失败");
    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "导入水质监测信息异常："+e.getMessage());
		}
		return result;
	}
}
