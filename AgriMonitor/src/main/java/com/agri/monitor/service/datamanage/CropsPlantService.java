package com.agri.monitor.service.datamanage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.agri.monitor.entity.CropsPlant;
import com.agri.monitor.entity.FarmInfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.CacheTypeEnum;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.CropsPlantMapper;
import com.agri.monitor.utils.CacheUtil;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.utils.TreeBuilder;
import com.agri.monitor.vo.CropsPlantQueryVO;
import com.agri.monitor.vo.Node;

@Service
public class CropsPlantService {
	
	private static final Logger logger = LoggerFactory.getLogger(CropsPlantService.class);
	
	@Autowired
	private CropsPlantMapper cropsPlantMapper;
	
	public CropsPlant findById(Integer gid, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("获取农作物产量信息，GID=" + gid);
		}
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询农作物产量信息，GID="+gid);
		return cropsPlantMapper.findById(gid);
	}
	
	public Map doDel(List<Integer> gids, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("农作物产量数据删除开始：" + gids);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			cropsPlantMapper.delete(gids);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "农作物产量数据删除，GID="+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除农作物产量信息异常" + e);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "农作物产量数据删除异常："+e.getMessage());
		}
		return result;
	}
	
	public Map saveOrUpdate(CropsPlant cropsPlant,String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("农作物产量数据更新开始：" + cropsPlant);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			cropsPlant.setModifier(userid);
			//更新
			if(!StringUtils.isEmpty(cropsPlant.getGid())) {
				cropsPlant.setLast_time(new Date());
				cropsPlantMapper.update(cropsPlant);
				LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "农作物产量数据更新"+cropsPlant);
			}else {//新增
				cropsPlant.setCreator(userid);
				cropsPlantMapper.insert(cropsPlant);
				LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, userid, "新增农作物产量数据"+cropsPlant);
			}
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("保存农作物产量信息异常" + e);
			LogUtil.log(LogOptTypeEnum.SAVE, LogOptSatusEnum.FAIL, userid, "保存农作物产量数据异常："+e.getMessage());
		}
		return result;
	}
	
	public List<Map> findAllForPage(CropsPlantQueryVO queryVO, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("查询所有农作物产量数据开始：" + queryVO);
		}
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询农作物产量信息，"+queryVO);
		return cropsPlantMapper.findAllForPage(queryVO);
	}
	
	public int findAllCount(CropsPlantQueryVO queryVO) {
		return cropsPlantMapper.findAllCount(queryVO);
	}
	
	public Map dataImport(MultipartFile file, HttpServletRequest request) {
		if (logger.isInfoEnabled()) {
			logger.info("农作物产量数据导入开始");
		}
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
	        if (logger.isInfoEnabled()) {
				logger.info("农作物产量数据导入，文件名：" + filename);
			}
	        if (prefix.equals("xlsx")) {
	        	wb = new XSSFWorkbook(file.getInputStream());
	        } else if (prefix.equals("xls")) {
	        	wb = new HSSFWorkbook(file.getInputStream());
	        } else {
	        	if (logger.isInfoEnabled()) {
					logger.info("农作物产量数据导入，不支持的文件类型");
				}
	        	result.put("code", -1);
	    		result.put("msg", "不支持的文件类型");
	    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(),"不支持的文件类型");
	    		return result;
	        }
	        
	        Sheet sheet1 = wb.getSheetAt(0);
	        int i = 0;
	        String towns = null;
	        Integer year = null;
	        List<CropsPlant> list = new ArrayList<>();
	        for (Row row : sheet1) {
	        	//解析所属乡镇
	           if (i == 1) {
	        	   towns = row.getCell(1).getStringCellValue();
	        	   if(StringUtils.isEmpty(towns)) {
	        		   result.put("code", -1);
	        		   result.put("msg", "乡镇未填写");
	        		   return result;
	        	   }
	           }
	           if(i == 2) {
	        	   String ymstr  = row.getCell(1).getStringCellValue();
	        	   if(StringUtils.isEmpty(ymstr)) {
	        		   result.put("code", -1);
	        		   result.put("msg", "报表年份不符合数字格式");
	        		   return result;
	        	   }
	        	   ymstr=ymstr.trim();
	        	    try {
	        	    	if(ymstr.length()!=4) {
	        	    		throw new Exception();
	        	    	}
	        	    	year=Integer.parseInt(ymstr);
					} catch (Exception e) {
						result.put("code", -1);
		        		result.put("msg", "报表年份不符合数字格式");
		        		return result;
					}
	           }
	           if (i >= 5) {
	        	   CropsPlant cropsPlant = new CropsPlant();
	        	   
	        	   String name=row.getCell(0).getStringCellValue();
	        	   if(StringUtils.isEmpty(name)) {
	        		   break;
	        	   }
	        	   name=name.trim();
	        	   Integer type = getType(name);
	        	   if(null == type) {
	        		   result.put("code", -1);
	        		   result.put("msg", "第"+(i+1)+"行农作物分类在系统中未维护");
	        		   return result;
	        	   }
	        	   cropsPlant.setCrops_type(type);
	        	   
	        	   cropsPlant.setPlanted_area(row.getCell(1).getNumericCellValue());
	        	   cropsPlant.setPlanted_output(row.getCell(2).getNumericCellValue());
	        	   cropsPlant.setCreator(user.getUser_id());
	        	   cropsPlant.setModifier(user.getUser_id());
	        	   cropsPlant.setCounty("刚察县");
	        	   cropsPlant.setTowns(towns);
	        	   cropsPlant.setDate_year(year);
	        	   list.add(cropsPlant);
	           }
	           i++;
	        }
	        cropsPlantMapper.batchInsert(list);
	        LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.SUCESS, user.getUser_id(), "导入农作物产量信息，共导入"+list.size()+"条");
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("农作物产量数据导入，解析文件异常", e);
			}
			result.put("code", -1);
    		result.put("msg", "解析文件失败");
    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "导入农作物产量信息异常："+e.getMessage());
		}
		return result;
	}
	
	public void downloadFile(HttpServletResponse response) {
		File destFile = null;
		InputStream is = null;
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			String staticDir = ResourceUtils.getURL("classpath:static").getPath();
			//复制原模板到临时文件中
			destFile = new File(staticDir+"/excel/"+new Date().getTime()+".xls");
			FileUtils.copyFile(ResourceUtils.getFile(staticDir+"/excel/cropsplant.xls"),destFile);
			is = new FileInputStream(destFile);
			POIFSFileSystem pfs = new POIFSFileSystem(is);
			//读取excel模板
			HSSFWorkbook wb = new HSSFWorkbook(pfs);
			HSSFSheet sheet = wb.getSheetAt(0);
			//获取指标数据
			List<Map> list = (List<Map>) CacheUtil.getCache(CacheTypeEnum.CROPSTYPE);
			List<Node> nodelist = new ArrayList<>();
			if (null != list && list.size() > 0) {
				for (int i = 0;i < list.size(); i++) {
					sheet.getRow(5 + i).getCell(0).setCellValue((String) list.get(i).get("type_name"));
				}
				//强制下载不打开
	    		response.setContentType("application/octet-stream");
	            //使用URLEncoder来防止文件名乱码或者读取错误
	            response.setHeader("Content-Disposition", "attachment; filename="+URLEncoder.encode("农作物产量情况报表.xls", "UTF-8"));
	            wb.write(out);
			}else {
				out.write(new String("系统未维护农作物类型信息，请联系管理员新增").getBytes("utf-8"));
			}
            
        } catch (Exception e) {
            logger.error("生成模板文件异常",e);
            try {
				out.write(new String("系统未维护农作物类型信息，请联系管理员新增").getBytes("utf-8"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        } finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (destFile != null) {
				destFile.delete();
			}
			if (out != null) {
				try {
					out.flush();
		            out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private Integer getType(String name) {
		if (StringUtils.isEmpty(name)) {
			return null;
		}
		List<Map> list = (List<Map>) CacheUtil.getCache(CacheTypeEnum.CROPSTYPE);
		if (list == null || list.size() == 0) {
			return null;
		}
		for (Map map : list) {
			if(name.equals(map.get("type_name"))) {
				return (Integer) map.get("gid");
			}
		}
		return null;
	}
}
