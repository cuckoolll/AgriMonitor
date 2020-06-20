package com.agri.monitor.service.datamanage;

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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.agri.monitor.entity.AgriBaseinfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.AgriBaseinfoMapper;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.vo.AgriBaseinfoQueryVO;

@Service
public class AgriBaseinfoService {
	
	private static final Logger logger = LoggerFactory.getLogger(AgriBaseinfoService.class);
	
	@Autowired
	private AgriBaseinfoMapper agriBaseinfoMapper;
	
	public AgriBaseinfo findById(Integer gid, String userid) {
		info("获取农业基本信息，GID=" + gid);
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询农业基本信息，GID="+gid);
		return agriBaseinfoMapper.findById(gid);
	}
	
	public Map doDel(List<Integer> gids, String userid) {
		info("农业基本数据删除开始：" + gids);
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			agriBaseinfoMapper.delete(gids);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "农业基本数据删除，GID="+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除农业基本信息异常" + e);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "农业基本数据删除异常："+e.getMessage());
		}
		return result;
	}
	
	public Map saveOrUpdate(AgriBaseinfo info,String userid) {
		info("农业基本数据更新开始：" + info);
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			info.setModifier(userid);
			//更新
			if(!StringUtils.isEmpty(info.getGid())) {
				info.setLast_time(new Date());
				agriBaseinfoMapper.update(info);
				LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "农业基本数据更新"+info);
			}else {//新增
				info.setCreator(userid);
				agriBaseinfoMapper.insert(info);
				LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, userid, "新增农业基本数据"+info);
			}
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("保存农业基本信息异常" + e);
			LogUtil.log(LogOptTypeEnum.SAVE, LogOptSatusEnum.FAIL, userid, "保存农业基本数据异常："+e.getMessage());
		}
		return result;
	}
	
	public List<Map> findAllForPage(AgriBaseinfoQueryVO queryVO, String userid) {
		info("查询所有农业基本数据开始：" + queryVO);
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询农业基本信息，"+queryVO);
		return agriBaseinfoMapper.findAllForPage(queryVO);
	}
	
	public List<Map> findNewData() {
		return agriBaseinfoMapper.findNewData();
	}
	
	public int findAllCount(AgriBaseinfoQueryVO queryVO) {
		return agriBaseinfoMapper.findAllCount(queryVO);
	}
	
	public Map dataImport(MultipartFile file, HttpServletRequest request) {
		info("农业基本数据导入开始");
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
        	info("农业基本数据导入，文件名：" + filename);
	        if (prefix.equals("xlsx")) {
	        	wb = new XSSFWorkbook(file.getInputStream());
	        } else if (prefix.equals("xls")) {
	        	wb = new HSSFWorkbook(file.getInputStream());
	        } else {
        		info("农业基本数据导入，不支持的文件类型");
	        	result.put("code", -1);
	    		result.put("msg", "不支持的文件类型");
	    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(),"不支持的文件类型");
	    		return result;
	        }
	        
	        Sheet sheet1 = wb.getSheetAt(0);
	        int i = 0;
	        List<AgriBaseinfo> list = new ArrayList<>();
	        for (Row row : sheet1) {
	           if (i >= 2) {
	        	   AgriBaseinfo info = new AgriBaseinfo();
	        	   String towns = null != row.getCell(0) ? row.getCell(0).getStringCellValue():null;
	        	   if(StringUtils.isEmpty(towns)) {
	        		   break;
	        	   }
	        	   info.setTowns(towns);
	        	   String village = null != row.getCell(1) ? row.getCell(1).getStringCellValue():null;
	        	   if(StringUtils.isEmpty(village)) {
	        		   break; 
	        	   }
	        	   
	        	   info.setVillage(village);
	        	   info.setRksl(null != row.getCell(2) ? (int) row.getCell(2).getNumericCellValue() : 0);
	        	   info.setGtmj(null != row.getCell(3) ? row.getCell(3).getNumericCellValue(): 0);
	        	   info.setGdmj(null != row.getCell(4) ? row.getCell(4).getNumericCellValue(): 0);
	        	   info.setGbzntmj(null != row.getCell(5) ? row.getCell(5).getNumericCellValue(): 0);
	        	   info.setCcmj(null != row.getCell(6) ? row.getCell(6).getNumericCellValue(): 0);
	        	   info.setNzwzl(null != row.getCell(7) ? row.getCell(7).getStringCellValue(): "");
	        	   info.setNzzmj(null != row.getCell(8) ? row.getCell(8).getNumericCellValue(): 0);
	        	   info.setZzfs(null != row.getCell(9) ? row.getCell(9).getStringCellValue(): "");
	        	   info.setCreator(user.getUser_id());
	        	   info.setModifier(user.getUser_id());
	        	   info.setCounty("刚察县");
	        	   list.add(info);
	        	   
	        	   Map map = new HashMap();
	        	   map.put("towns", towns);
	        	   map.put("village", village);
	        	   agriBaseinfoMapper.deleteByTows(map);
	           }
	           i++;
	        }
	        agriBaseinfoMapper.batchInsert(list);
	        LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.SUCESS, user.getUser_id(), "导入农业基本信息，共导入"+list.size()+"条");
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("农业基本数据导入，解析文件异常", e);
			}
			result.put("code", -1);
    		result.put("msg", "解析文件失败");
    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "导入农业基本信息异常："+e.getMessage());
		}
		return result;
	}
	
	private void info(String msg) {
		if (logger.isInfoEnabled()) {
			logger.info(msg);
		}
	}
}
