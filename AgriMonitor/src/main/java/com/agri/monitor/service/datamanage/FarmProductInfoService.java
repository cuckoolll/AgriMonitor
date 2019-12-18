package com.agri.monitor.service.datamanage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.agri.monitor.entity.FarmProductInfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.FarmProductInfoMapper;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.vo.FarmProductQueryVO;

@Service
@Transactional
public class FarmProductInfoService {
	
	private static final Logger logger = LoggerFactory.getLogger(FarmProductInfoService.class);
	
	private SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private FarmProductInfoMapper farmProductInfoMapper;
	 
	public Map queryInfoForPage(FarmProductQueryVO queryVo, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("获取农产品信息，入参=date_year:" + queryVo.getDate_year());
		}

		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		result.put("msg", "成功");
		result.put("count", farmProductInfoMapper.queryInfoCount(queryVo));
		result.put("data", farmProductInfoMapper.queryInfoForPage(queryVo));
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, 
				"获取农产品信息，入参=date_year:" + queryVo.getDate_year());
		return result;
	}
	
	public FarmProductInfo findById(Integer gid, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("获取农产品信息，GID=" + gid);
		}
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询农产品信息，GID="+gid);
		return farmProductInfoMapper.findById(gid);
	}
	
	public Map saveOrUpdate(FarmProductInfo farmproductinfo, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("农产品数据更新开始：" + farmproductinfo);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			farmproductinfo.setModifier(String.valueOf(userid));
			//更新
			if(!StringUtils.isEmpty(farmproductinfo.getGid())) {
				farmProductInfoMapper.updateInfo(farmproductinfo);
				LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "农产品数据更新"+farmproductinfo);
			}else {//新增
				farmproductinfo.setCreator(String.valueOf(userid));
				farmProductInfoMapper.insertInfo(farmproductinfo);
				LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, userid, "新增农产品数据"+farmproductinfo);
			}
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("保存农产品数据异常" + e);
			LogUtil.log(LogOptTypeEnum.SAVE, LogOptSatusEnum.FAIL, userid, "保存农产品数据异常："+e.getMessage());
		}
		return result;
	}
	
	public Map dataImport(MultipartFile file, HttpServletRequest request) {
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
	        if (prefix.equals("xlsx")) {
	        	wb = new XSSFWorkbook(file.getInputStream());
	        } else if (prefix.equals("xls")) {
	        	wb = new HSSFWorkbook(file.getInputStream());
	        } else {
	        	result.put("code", -1);
	    		result.put("msg", "不支持的文件类型");
	    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(),"不支持的文件类型");
	    		return result;
	        }
	        
	        Sheet sheet1 = wb.getSheetAt(0);
	        int i = 0;
	        List<FarmProductInfo> list = new ArrayList<>();
	        for (Row row : sheet1) {
	           if (i >= 4) {
	        	   if(row.getCell(0)==null) {
	        		   break;
	        	   }
	        	   FarmProductInfo info = new FarmProductInfo();
	        	   info.setDate_year(row.getCell(0).getNumericCellValue()+"");
	        	   info.setPlant_product_count(row.getCell(1)==null?0:(int)row.getCell(1).getNumericCellValue());
	        	   info.setAnimal_product_count(row.getCell(2)==null?0:(int)row.getCell(2).getNumericCellValue());
	        	   info.setFish_product_count(row.getCell(3)==null?0:(int)row.getCell(3).getNumericCellValue());
	        	   info.setHigh_quality_plant_count(row.getCell(4)==null?0:(int)row.getCell(4).getNumericCellValue());
	        	   info.setHigh_quality_animal_count(row.getCell(5)==null?0:(int)row.getCell(5).getNumericCellValue());
	        	   info.setHigh_quality_fish_count(row.getCell(6)==null?0:(int)row.getCell(6).getNumericCellValue());
	        	   info.setCreator(user.getUser_id());
	        	   info.setModifier(user.getUser_id());
	        	   list.add(info);
	           }
	           i++;
	        }
	        farmProductInfoMapper.batchInsert(list);
	        LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.SUCESS, user.getUser_id(), "导入农产品数据，共导入"+list.size()+"条");
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("农产品数据导入，解析文件异常", e);
			}
			result.put("code", -1);
    		result.put("msg", "解析文件失败");
    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "导入农产品数据异常："+e.getMessage());
		}
		return result;
	}
	
	public Map delInfoByGid(List<Integer> gids, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("农产品数据删除开始：" + gids);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			farmProductInfoMapper.delInfoByGid(gids);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "农产品数据删除，GID="+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除农产品信息异常" + e);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "农产品删除异常："+e.getMessage());
		}
		return result;
	}
	
}
