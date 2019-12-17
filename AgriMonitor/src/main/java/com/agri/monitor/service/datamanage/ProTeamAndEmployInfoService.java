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

import com.agri.monitor.entity.ProTeamAndEmployInfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.ProTeamAndEmployInfoMapper;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.vo.ProTeamAndEmployInfoQueryVO;

@Service
public class ProTeamAndEmployInfoService {
	
	private static final Logger logger = LoggerFactory.getLogger(ProTeamAndEmployInfoService.class);
	
	@Autowired
	private ProTeamAndEmployInfoMapper proTeamAndEmployInfoMapper;
	
	public ProTeamAndEmployInfo findById(Integer gid, String userid) {
		info("获取农业生产组织及从业人员信息，GID=" + gid);
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询农业生产组织及从业人员信息，GID="+gid);
		return proTeamAndEmployInfoMapper.findById(gid);
	}
	
	public Map doDel(List<Integer> gids, String userid) {
		info("农业生产组织及从业人员数据删除开始：" + gids);
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			proTeamAndEmployInfoMapper.delete(gids);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "农业生产组织及从业人员数据删除，GID="+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除农业生产组织及从业人员信息异常" + e);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "农业生产组织及从业人员数据删除异常："+e.getMessage());
		}
		return result;
	}
	
	public Map saveOrUpdate(ProTeamAndEmployInfo info,String userid) {
		info("农业生产组织及从业人员数据更新开始：" + info);
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			info.setModifier(userid);
			//更新
			if(!StringUtils.isEmpty(info.getGid())) {
				info.setLast_time(new Date());
				proTeamAndEmployInfoMapper.update(info);
				LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "农业生产组织及从业人员数据更新"+info);
			}else {//新增
				info.setCreator(userid);
				proTeamAndEmployInfoMapper.insert(info);
				LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, userid, "新增农业生产组织及从业人员数据"+info);
			}
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("保存农业生产组织及从业人员信息异常" + e);
			LogUtil.log(LogOptTypeEnum.SAVE, LogOptSatusEnum.FAIL, userid, "保存农业生产组织及从业人员数据异常："+e.getMessage());
		}
		return result;
	}
	
	public List<Map> findAllForPage(ProTeamAndEmployInfoQueryVO queryVO, String userid) {
		info("查询所有农业生产组织及从业人员数据开始：" + queryVO);
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询农业生产组织及从业人员信息，"+queryVO);
		return proTeamAndEmployInfoMapper.findAllForPage(queryVO);
	}
	
	public int findAllCount(ProTeamAndEmployInfoQueryVO queryVO) {
		return proTeamAndEmployInfoMapper.findAllCount(queryVO);
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
	        List<ProTeamAndEmployInfo> list = new ArrayList<>();
	        for (Row row : sheet1) {
	           if (i >= 3) {
	        	   ProTeamAndEmployInfo info = new ProTeamAndEmployInfo();
	        	   info.setCounty("刚察县");
	        	   String towns = row.getCell(0).getStringCellValue();
	        	   if(StringUtils.isEmpty(towns)) {
	        		   break; 
	        	   }
	        	   info.setTowns(towns);
	        	   info.setVillage(row.getCell(1).getStringCellValue());
	        	   if (row.getCell(2) == null) {
	        		   break; 
	        	   }
	        	   info.setDate_year((int) row.getCell(2).getNumericCellValue());
	        	   info.setHs(row.getCell(3)==null?0:(int) row.getCell(3).getNumericCellValue()); 
	        	   info.setRks_n(row.getCell(4)==null?0:(int) row.getCell(4).getNumericCellValue());
	        	   info.setRks_v(row.getCell(5)==null?0:(int) row.getCell(5).getNumericCellValue());
	        	   info.setNyrs(row.getCell(6)==null?0:(int) row.getCell(6).getNumericCellValue());
	        	   info.setCmyrs(row.getCell(7)==null?0:(int) row.getCell(7).getNumericCellValue());
	        	   info.setLyrs(row.getCell(8)==null?0:(int) row.getCell(8).getNumericCellValue());
	        	   info.setFwyrs(row.getCell(9)==null?0:(int) row.getCell(9).getNumericCellValue());
	        	   info.setBz(row.getCell(10)==null?"":row.getCell(10).getStringCellValue());
	        	   info.setCreator(user.getUser_id());
	        	   info.setModifier(user.getUser_id());
	        	   list.add(info);
	           }
	           i++;
	        }
	        proTeamAndEmployInfoMapper.batchInsert(list);
	        LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.SUCESS, user.getUser_id(), "导入农民生活情况信息，共导入"+list.size()+"条");
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("农民生活情况信息导入，解析文件异常", e);
			}
			result.put("code", -1);
    		result.put("msg", "解析文件失败");
    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "导入农民生活情况信息异常："+e.getMessage());
		}
		return result;
	}
	
	private void info(String msg) {
		if (logger.isInfoEnabled()) {
			logger.info(msg);
		}
	}
}
