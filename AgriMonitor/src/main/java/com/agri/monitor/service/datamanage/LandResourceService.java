package com.agri.monitor.service.datamanage;

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

import com.agri.monitor.entity.FishInfo;
import com.agri.monitor.entity.LandResource;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.LandResourceMapper;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.vo.LandResourceQueryVO;

@Service
public class LandResourceService {
	
	private static final Logger logger = LoggerFactory.getLogger(LandResourceService.class);
	
	@Autowired
	private LandResourceMapper landResourceMapper;
	
	public LandResource findById(Integer gid, String userid) {
		info("获取土地资源信息，GID=" + gid);
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询土地资源信息，GID="+gid);
		return landResourceMapper.findById(gid);
	}
	
	public Map doDel(List<Integer> gids, String userid) {
		info("土地资源数据删除开始：" + gids);
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			landResourceMapper.delete(gids);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "土地资源数据删除，GID="+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除土地资源信息异常" + e);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "土地资源数据删除异常："+e.getMessage());
		}
		return result;
	}
	
	public Map saveOrUpdate(LandResource info,String userid) {
		info("土地资源数据更新开始：" + info);
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			//更新
			if(!StringUtils.isEmpty(info.getGid())) {
				landResourceMapper.update(info);
				LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "土地资源数据更新"+info);
			}else {//新增
				landResourceMapper.insert(info);
				LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, userid, "新增土地资源数据"+info);
			}
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("保存土地资源信息异常" + e);
			LogUtil.log(LogOptTypeEnum.SAVE, LogOptSatusEnum.FAIL, userid, "保存土地资源数据异常："+e.getMessage());
		}
		return result;
	}
	
	public List<Map> findAllForPage(LandResourceQueryVO queryVO, String userid) {
		info("查询所有土地资源数据开始：" + queryVO);
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询土地资源信息，"+queryVO);
		return landResourceMapper.findAllForPage(queryVO);
	}
	
	public int findAllCount(LandResourceQueryVO queryVO) {
		return landResourceMapper.findAllCount(queryVO);
	}
	
	private void info(String msg) {
		if (logger.isInfoEnabled()) {
			logger.info(msg);
		}
	}
	
	public Map dataImport(MultipartFile file, HttpServletRequest request) {
		if (logger.isInfoEnabled()) {
			logger.info("导入土地资源信息-----------------开始");
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
				logger.info("导入土地资源信息，文件名：" + filename);
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
	 				logger.info("导入土地资源信息，不支持的文件类型");
	 			}
	    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(),"导入土地资源信息，不支持的文件类型");
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
	        	   LandResource landresource = new LandResource();
	        	   landresource.setCounty(county);
	        	   landresource.setTowns(towns);
	        	   landresource.setYear(Integer.valueOf(row.getCell(0).getStringCellValue()));
	        	   
	        	   landresource.setGdmj(row.getCell(1).getNumericCellValue());
	        	   landresource.setGbzltmj(row.getCell(2).getNumericCellValue());
	        	   landresource.setGbzltbz(row.getCell(3).getNumericCellValue());
	        	   landresource.setGgslyxs(row.getCell(4).getNumericCellValue());
	        	   landresource.setQkmj(row.getCell(5).getNumericCellValue());
	        	   landresource.setYcmj(row.getCell(6).getNumericCellValue());
	        	   landresource.setYmmj(row.getCell(7).getNumericCellValue());
	        	   landresource.setDc(row.getCell(8).getNumericCellValue());
	        	   landresource.setZc(row.getCell(9).getNumericCellValue());
	        	   
	        	   try {
	        		   String gid = landResourceMapper.queryGid(landresource.getYear());
		        	   if (StringUtils.isEmpty(gid)) {
		        		   landResourceMapper.insert(landresource);
		        	   } else {
		        		   landresource.setGid(Integer.valueOf(gid));
		        		   landResourceMapper.update(landresource);
		        	   }
	        	   } catch(Exception e) {
        			   if (logger.isErrorEnabled()) {
        					logger.error("土地资源信息插入数据失败", e);
        				}
        				result.put("code", -1);
        	    		result.put("msg", "土地资源信息插入数据失败");
        	    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "导入土地资源信息异常："+e.getMessage());
        		   }
	           }
	           i++;
	        }
	        LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, user.getUser_id(), "导入土地资源信息成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", -1);
    		result.put("msg", "解析文件失败");
    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "导入土地资源信息异常："+e.getMessage());
		}
		return result;
	}
}
