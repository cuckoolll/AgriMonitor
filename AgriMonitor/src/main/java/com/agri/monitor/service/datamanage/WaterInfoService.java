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
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.entity.WaterInfo;
import com.agri.monitor.mapper.WaterInfoMapper;

@Service
@Transactional
public class WaterInfoService {
	
	private static final Logger logger = LoggerFactory.getLogger(WaterInfoService.class);
	
	@Autowired
	private WaterInfoMapper waterInfoMapper;
	
	@Value("${spring.profiles.active}")  
	private String active;  
	
	public List<WaterInfo> findAll() {
		return waterInfoMapper.findAll();
	}
	
	public List<WaterInfo> queryInfoByCountryAndTime(final String county, final String time) {
		return waterInfoMapper.queryInfoByCountryAndTime(county, time);
	}
	
	public Map dataImport(MultipartFile file, HttpServletRequest request) {
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
	        }
	        
	        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        
	        Sheet sheet1 = wb.getSheetAt(0);
	        int i = 0;
	        UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
	        String county = null;
        	String quality_address = null;
        	String quality_time = null;
	        
	        for (Row row : sheet1) {
	        	//解析所属乡镇
	           if (i == 1) {
	        	   county = row.getCell(0).getStringCellValue();
	        	   quality_address = row.getCell(1).getStringCellValue();
	        	   quality_time = row.getCell(2).getStringCellValue();
	        	   //TODO 与缓存中乡镇信息对比
	           }
	           if (i >= 4) {
	        	   WaterInfo waterinfo = new WaterInfo();
	        	   waterinfo.setQuality_address(quality_address);
	        	   waterinfo.setCounty(county);
	        	   waterinfo.setQuality_time(df.parse(quality_time));
	        	   waterinfo.setQuality_type(row.getCell(0).getStringCellValue());
	        	   waterinfo.setQuality_result(row.getCell(1).getStringCellValue());
	        	   waterinfo.setRemarks(row.getCell(2).getStringCellValue());
	        	   waterinfo.setCreator(String.valueOf(user.getUser_id()));
	        	   waterinfo.setModifier(String.valueOf(user.getUser_id()));
	        	   waterinfo.setCreate_time(new Date());
	        	   
	        	   String gid = waterInfoMapper.queryGid(county, quality_time, row.getCell(0).getStringCellValue());
	        	   
	        	   if (StringUtils.isEmpty(gid)) {
	        		   waterInfoMapper.insertWaterInfo(waterinfo);
	        	   } else {
	        		   waterinfo.setGid(Integer.valueOf(gid));
	        		   waterInfoMapper.updateWaterInfo(waterinfo);
	        	   }
	           }
	           i++;
	        }
	        
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", -1);
    		result.put("msg", "解析文件失败");
		}
		return result;
	}
}
