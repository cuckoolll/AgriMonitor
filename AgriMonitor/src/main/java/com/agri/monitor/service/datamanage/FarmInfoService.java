package com.agri.monitor.service.datamanage;

import java.io.IOException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.agri.monitor.entity.FarmInfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.mapper.FarmInfoMapper;

@Service
public class FarmInfoService {
	@Autowired
	private FarmInfoMapper farmInfoMapper;
	
	public List<Map> findAll(Map map) {
		return farmInfoMapper.findAll(map);
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
	        
	        Sheet sheet1 = wb.getSheetAt(0);
	        int i = 0;
	        String towns = null;
	        UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
	        List<FarmInfo> list = new ArrayList<>();
	        for (Row row : sheet1) {
	        	//解析所属乡镇
	           if (i == 1) {
	        	   towns = row.getCell(1).getStringCellValue();
	        	   //TODO 与缓存中乡镇信息对比
	           }
	           if (i >= 3) {
	        	   FarmInfo farminfo = new FarmInfo();
	        	   farminfo.setFarm_name(row.getCell(0).getStringCellValue());
	        	   farminfo.setFarm_address(row.getCell(1).getStringCellValue());
	        	   farminfo.setLegal_person(row.getCell(2).getStringCellValue());
	        	   farminfo.setPhone_num(row.getCell(3).getStringCellValue());
	        	   //认定畜种从缓存中获取GID
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
	        System.out.println(list);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", -1);
    		result.put("msg", "解析文件失败");
		}
		return result;
	}
}
