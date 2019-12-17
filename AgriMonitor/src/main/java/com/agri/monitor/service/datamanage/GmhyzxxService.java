package com.agri.monitor.service.datamanage;

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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.agri.monitor.entity.Gmhyzxx;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.GmhyzxxMapper;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.vo.GmhyzxxQueryVO;

@Service
public class GmhyzxxService {
	
	private static final Logger logger = LoggerFactory.getLogger(GmhyzxxService.class);
	
	@Autowired
	private GmhyzxxMapper gmhyzxxMapper;
	
	public Gmhyzxx findById(Integer gid, String userid) {
		info("获取规模化养殖信息，GID=" + gid);
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询规模化养殖信息，GID="+gid);
		return gmhyzxxMapper.findById(gid);
	}
	
	public Map doDel(List<Integer> gids, String userid) {
		info("规模化养殖数据删除开始：" + gids);
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			gmhyzxxMapper.delete(gids);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "规模化养殖数据删除，GID="+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除规模化养殖信息异常" + e);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "规模化养殖数据删除异常："+e.getMessage());
		}
		return result;
	}
	
	public Map saveOrUpdate(Gmhyzxx info,String userid) {
		info("规模化养殖数据更新开始：" + info);
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			//更新
			if(!StringUtils.isEmpty(info.getGid())) {
				gmhyzxxMapper.update(info);
				LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "规模化养殖数据更新"+info);
			}else {//新增
				gmhyzxxMapper.insert(info);
				LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, userid, "新增规模化养殖数据"+info);
			}
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("保存规模化养殖信息异常" + e);
			LogUtil.log(LogOptTypeEnum.SAVE, LogOptSatusEnum.FAIL, userid, "保存规模化养殖数据异常："+e.getMessage());
		}
		return result;
	}
	
	public List<Map> findAllForPage(GmhyzxxQueryVO queryVO, String userid) {
		info("查询所有规模化养殖数据开始：" + queryVO);
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询规模化养殖信息，"+queryVO);
		return gmhyzxxMapper.findAllForPage(queryVO);
	}
	
	public int findAllCount(GmhyzxxQueryVO queryVO) {
		return gmhyzxxMapper.findAllCount(queryVO);
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
	        List<Gmhyzxx> list = new ArrayList<>();
	        for (Row row : sheet1) {
	           if (i >= 2) {
	        	   Gmhyzxx info = new Gmhyzxx();
	        	   info.setYear(Integer.valueOf(row.getCell(0).getStringCellValue()));
	        	   info.setSzcls(Double.valueOf(row.getCell(1).getStringCellValue()));
	        	   info.setRncls(Double.valueOf(row.getCell(2).getStringCellValue()));
	        	   info.setNncls(Double.valueOf(row.getCell(3).getStringCellValue()));
	        	   info.setRycls(Double.valueOf(row.getCell(4).getStringCellValue()));
	        	   info.setDjcls(Double.valueOf(row.getCell(5).getStringCellValue()));
	        	   info.setRjcls(Double.valueOf(row.getCell(6).getStringCellValue()));
	        	   info.setQt(Double.valueOf(row.getCell(7).getStringCellValue()));
	        	   list.add(info);
	           }
	           i++;
	        }
	        gmhyzxxMapper.batchInsert(list);
	        LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.SUCESS, user.getUser_id(), "导入规模化畜禽养殖量信息，共导入"+list.size()+"条");
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("规模化畜禽养殖量信息导入，解析文件异常", e);
			}
			result.put("code", -1);
    		result.put("msg", "解析文件失败");
    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "导入规模化畜禽养殖量信息异常："+e.getMessage());
		}
		return result;
	}
	
	private void info(String msg) {
		if (logger.isInfoEnabled()) {
			logger.info(msg);
		}
	}
}
