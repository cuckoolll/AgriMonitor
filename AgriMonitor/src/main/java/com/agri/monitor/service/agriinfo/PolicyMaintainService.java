package com.agri.monitor.service.agriinfo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.agri.monitor.entity.PolicyInfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.entity.WaterInfo;
import com.agri.monitor.enums.CacheTypeEnum;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.PolicyMaintainMapper;
import com.agri.monitor.utils.CacheUtil;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.vo.PolicyQueryVO;

@Service
@Transactional
public class PolicyMaintainService {
	
	private static final Logger logger = LoggerFactory.getLogger(PolicyMaintainService.class);
	
	private SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private PolicyMaintainMapper policyMaintainMapper;
	 
	public Map queryInfoForPage(PolicyQueryVO queryVo, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("获取农业政策文件信息，入参=file_name:" + queryVo.getFile_name() 
				+ ", create_time:" + queryVo.getCreate_time() + ", info_type:" + queryVo.getInfo_type());
		}

		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		result.put("msg", "成功");
		result.put("count", policyMaintainMapper.queryInfoCount(queryVo));
		List<Map> list = policyMaintainMapper.queryInfoForPage(queryVo);
		List<Map> type = getInfoType();
		for (Map info : list) {
			for (Map map : type) {
				String info_type = String.valueOf((Integer) info.get("info_type"));
				if (((String) map.get("id")).equals(info_type)) {
					info.put("info_type_name", (String)map.get("text"));
					continue;
				}
			}
		}
		result.put("data", list);
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, 
				"获取农业政策文件信息，入参=file_name:" + queryVo.getFile_name() 
				+ ", create_time:" + queryVo.getCreate_time()
				+ ", info_type:" + queryVo.getInfo_type());
		return result;
	}
	
	public Map delInfoByGid(List<Integer> gids, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("农业政策文件信息删除开始：" + gids);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			policyMaintainMapper.delInfoByGid(gids);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "农业政策文件信息删除，GID="+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除农业政策文件信息异常" + e);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "农业政策文件信息删除异常："+e.getMessage());
		}
		return result;
	}
	
	public Map fileUpload(MultipartFile file, HttpServletRequest request) {
		if (logger.isInfoEnabled()) {
			logger.info("上传农业政策文件-----------------开始");
		}
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		final Map<String, Object> result = new HashMap<String, Object>();

		if (file.isEmpty()) {
			result.put("code", -1);
			result.put("msg", "上传失败，请选择文件");
			if (logger.isInfoEnabled()) {
				logger.info("农业政策文件,上传失败，请选择文件");
			}
			LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "农业政策文件上传失败，请选择文件");
            return result;
        }

	    String fileName = file.getOriginalFilename();
	    String filePath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/policyfile/";
	    String file_address = filePath + fileName;
	    String file_name = request.getParameter("file_name");
	    String info_type = request.getParameter("info_type");
	    String company = request.getParameter("company");
	    String gid = null;
	    try {
	    	gid = policyMaintainMapper.queryGid(file_name);
	    } catch (Exception e) {
	    	logger.error("农业政策文件,上传失败", e);
        	LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "农业政策文件,上传失败" + e.getMessage());
        	result.put("code", -1);
    		result.put("msg", "上传失败");
    		return result;
	    }
	    
	    if (!StringUtils.isEmpty(gid)) {
	    	if (logger.isInfoEnabled()) {
            	logger.info("农业政策文件,当前上传文件标题已存在");
            }
	    	LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "农业政策文件,当前上传文件标题已存在");
	    	result.put("code", -1);
    		result.put("msg", "当前上传文件标题已存在");
    		return result;
	    } 
	    
        File dest = new File(file_address);
        if (!dest.exists()) {
        	dest.mkdirs();
        }
        try {
            file.transferTo(dest);
            if (logger.isInfoEnabled()) {
            	logger.info("农业政策文件,上传成功");
            }
            LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.SUCESS, user.getUser_id(), "农业政策文件,上传成功");
            
        } catch (IOException e) {
        	logger.error("农业政策文件,上传失败", e);
        	LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "农业政策文件,上传失败" + e.getMessage());
        	result.put("code", -1);
    		result.put("msg", "上传失败");
    		return result;
        }
        
        PolicyInfo policy = new PolicyInfo();
	    policy.setFile_name(file_name);
	    policy.setFile_address(file_address);
	    policy.setCreator(user.getUser_id());
	    policy.setCompany(company);
	    policy.setInfo_type(info_type);
	    try {
	    	policyMaintainMapper.insertInfo(policy);
	    } catch (Exception e) {
	    	logger.error("农业政策文件,上传失败,保存文件信息错误", e);
        	LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(),
        			"农业政策文件,上传失败,保存文件信息错误" + e.getMessage());
        	result.put("code", -1);
    		result.put("msg", "上传失败");
    		return result;
	    }
	    
	    if (logger.isInfoEnabled()) {
			logger.info("上传农业政策文件-----------------成功");
		}
		result.put("code", 0);
		result.put("msg", "上传成功");
		return result;
	}
	
	/**
	 * 下载 .
	 * @param response .
	 */
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) {
		UserInfo user = (UserInfo) request.getSession().getAttribute("userinfo");
		String file_address = request.getParameter("file_address");
		String[] addressArray = file_address.split("/");
		String fileName = addressArray[addressArray.length - 1];
		
		if (logger.isInfoEnabled()) {
			logger.info("下载农业政策文件：file_address=" + file_address);
		}
		
		 try {
	            InputStream bis = new BufferedInputStream(new FileInputStream(new File(file_address)));
	            fileName = URLEncoder.encode(fileName,"UTF-8");
	            //设置文件下载头
	            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
	            //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
	            response.setContentType("multipart/form-data");
	            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
	            int len = 0;
	            while((len = bis.read()) != -1){
	                out.write(len);
	                out.flush();
	            }
	            if (logger.isInfoEnabled()) {
	    			logger.info("下载农业政策文件 -----------------成功");
	    		}
	            out.close();
	        } catch (Exception e) {
	            logger.error("下载农业政策文件错误", e);
	            LogUtil.log(LogOptTypeEnum.DOWNLOAD, LogOptSatusEnum.FAIL, user.getUser_id(), "下载农业政策文件错误" + e.getMessage());
	        }
	}
	
	public List<Map> getInfoType() {
		List<Map> infoTypeList = new ArrayList();
		
		Map infoTypeMap0 = new HashMap();
		infoTypeMap0.put("id", "0");
		infoTypeMap0.put("text", "项目申报文件");
		infoTypeList.add(infoTypeMap0);
		
		Map infoTypeMap1 = new HashMap();
		infoTypeMap1.put("id", "1");
		infoTypeMap1.put("text", "项目技术文档");
		infoTypeList.add(infoTypeMap1);
		
		Map infoTypeMap2 = new HashMap();
		infoTypeMap2.put("id", "2");
		infoTypeMap2.put("text", "政策法规");
		infoTypeList.add(infoTypeMap2);
		
		Map infoTypeMap3 = new HashMap();
		infoTypeMap3.put("id", "3");
		infoTypeMap3.put("text", "通知公告");
		infoTypeList.add(infoTypeMap3);
		
		return infoTypeList;
	}
}
