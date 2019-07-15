package com.agri.monitor.service.agriinfo;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.agri.monitor.entity.PolicyInfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.entity.WaterInfo;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.PolicyMaintainMapper;
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
				+ ", create_time:" + queryVo.getCreate_time());
		}

		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		result.put("msg", "成功");
		result.put("count", policyMaintainMapper.queryInfoCount(queryVo));
		result.put("data", policyMaintainMapper.queryInfoForPage(queryVo));
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, 
				"获取农业政策文件信息，入参=file_name:" + queryVo.getFile_name() + ", create_time:" + queryVo.getCreate_time());
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
	    String filePath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/policyfile";
	    String file_address = filePath + fileName;
	    String file_name = request.getParameter("file_name");
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
	    policy.setCreate_time(user.getUser_id());
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
	    
		result.put("code", 0);
		result.put("msg", "上传成功");
		return result;
	}
}
