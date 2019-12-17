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

import com.agri.monitor.entity.AgriInputinfo;
import com.agri.monitor.entity.FarmProductInfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.AgriInputinfoMapper;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.vo.AgriInputinfoQueryVO;

@Service
public class AgriInputinfoService {
	
	private static final Logger logger = LoggerFactory.getLogger(AgriInputinfoService.class);
	
	@Autowired
	private AgriInputinfoMapper agriInputinfoMapper;
	
	public AgriInputinfo findById(Integer gid, String userid) {
		info("获取农业投入信息，GID=" + gid);
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询农业投入信息，GID="+gid);
		return agriInputinfoMapper.findById(gid);
	}
	
	public Map doDel(List<Integer> gids, String userid) {
		info("农业投入数据删除开始：" + gids);
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			agriInputinfoMapper.delete(gids);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "农业投入数据删除，GID="+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除农业投入信息异常" + e);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "农业投入数据删除异常："+e.getMessage());
		}
		return result;
	}
	
	public Map saveOrUpdate(AgriInputinfo info,String userid) {
		info("农业投入数据更新开始：" + info);
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			//更新
			if(!StringUtils.isEmpty(info.getGid())) {
				agriInputinfoMapper.update(info);
				LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "农业投入数据更新"+info);
			}else {//新增
				agriInputinfoMapper.insert(info);
				LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, userid, "新增农业投入数据"+info);
			}
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("保存农业投入信息异常" + e);
			LogUtil.log(LogOptTypeEnum.SAVE, LogOptSatusEnum.FAIL, userid, "保存农业投入数据异常："+e.getMessage());
		}
		return result;
	}
	
	public List<Map> findAllForPage(AgriInputinfoQueryVO queryVO, String userid) {
		info("查询所有农业投入数据开始：" + queryVO);
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询农业投入信息，"+queryVO);
		return agriInputinfoMapper.findAllForPage(queryVO);
	}
	
	public int findAllCount(AgriInputinfoQueryVO queryVO) {
		return agriInputinfoMapper.findAllCount(queryVO);
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
	        List<AgriInputinfo> list = new ArrayList<>();
	        for (Row row : sheet1) {
	           if (i >= 5) {
	        	   AgriInputinfo info = new AgriInputinfo();
	        	   info.setYear(Integer.valueOf(row.getCell(0).getStringCellValue()));
	        	   info.setN(Double.valueOf(row.getCell(1).getStringCellValue()));
	        	   info.setP(Double.valueOf(row.getCell(2).getStringCellValue()));
	        	   info.setK(Double.valueOf(row.getCell(3).getStringCellValue()));
	        	   info.setFhf(Double.valueOf(row.getCell(4).getStringCellValue()));
	        	   info.setYjf(Double.valueOf(row.getCell(5).getStringCellValue()));
	        	   info.setLjf(Double.valueOf(row.getCell(6).getStringCellValue()));
	        	   info.setDm_syl(Double.valueOf(row.getCell(7).getStringCellValue()));
	        	   info.setDm_fgmj(Double.valueOf(row.getCell(8).getStringCellValue()));
	        	   info.setLyqd(Double.valueOf(row.getCell(9).getStringCellValue()));
	        	   info.setLysyl(Double.valueOf(row.getCell(10).getStringCellValue()));
	        	   info.setLycyl(Double.valueOf(row.getCell(11).getStringCellValue()));
	        	   info.setJgzl(Double.valueOf(row.getCell(12).getStringCellValue()));
	        	   info.setZhlyjg(Double.valueOf(row.getCell(13).getStringCellValue()));
	        	   info.setJgksj(Double.valueOf(row.getCell(14).getStringCellValue()));
	        	   info.setLmzl(Double.valueOf(row.getCell(15).getStringCellValue()));
	        	   info.setLmsyl(Double.valueOf(row.getCell(16).getStringCellValue()));
	        	   info.setQcfl(Double.valueOf(row.getCell(17).getStringCellValue()));
	        	   info.setQcfzl(Double.valueOf(row.getCell(18).getStringCellValue()));
	        	   info.setBz(row.getCell(19).getStringCellValue());
	        	   list.add(info);
	           }
	           i++;
	        }
	        agriInputinfoMapper.batchInsert(list);
	        LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.SUCESS, user.getUser_id(), "导入农业投入品及废弃物信息，共导入"+list.size()+"条");
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("农业投入品及废弃物信息导入，解析文件异常", e);
			}
			result.put("code", -1);
    		result.put("msg", "解析文件失败");
    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "导入农业投入品及废弃物信息异常："+e.getMessage());
		}
		return result;
	}
	
	private void info(String msg) {
		if (logger.isInfoEnabled()) {
			logger.info(msg);
		}
	}
}
