package com.agri.monitor.service.datamanage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
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

import com.agri.monitor.entity.AnimalsBreed;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.CacheTypeEnum;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.AnimalsBreedMapper;
import com.agri.monitor.utils.CacheUtil;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.utils.TreeBuilder;
import com.agri.monitor.vo.AnimalsBreedQueryVO;
import com.agri.monitor.vo.Node;

@Service
public class AnimalsBreedService {
	
	private static final Logger logger = LoggerFactory.getLogger(AnimalsBreedService.class);
	
	private static ThreadLocal<Integer> rownum_local = new ThreadLocal<>(); 
	
	@Autowired
	private AnimalsBreedMapper animalsBreedMapper;
	
	public AnimalsBreed findById(Integer gid, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("获取养殖信息，GID=" + gid);
		}
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询畜牧业生产情况信息，GID="+gid);
		return animalsBreedMapper.findById(gid);
	}
	
	public Map doDel(List<Integer> gids, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("畜牧业生产情况数据删除开始：" + gids);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			animalsBreedMapper.delete(gids);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "畜牧业生产情况数据删除，GID="+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除畜牧业生产情况信息异常" + e);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "畜牧业生产情况数据删除异常："+e.getMessage());
		}
		return result;
	}
	
	public Map saveOrUpdate(AnimalsBreed animalsBreed,String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("畜牧业生产情况数据更新开始：" + animalsBreed);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			animalsBreed.setModifier(userid);
			//更新
			if(!StringUtils.isEmpty(animalsBreed.getGid())) {
				animalsBreed.setLast_time(new Date());
				animalsBreedMapper.update(animalsBreed);
				LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "畜牧业生产情况数据更新"+animalsBreed);
			}else {//新增
				animalsBreed.setCreator(userid);
				animalsBreedMapper.insert(animalsBreed);
				LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, userid, "新增畜牧业生产情况数据"+animalsBreed);
			}
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("保存畜牧业生产情况信息异常" + e);
			LogUtil.log(LogOptTypeEnum.SAVE, LogOptSatusEnum.FAIL, userid, "保存畜牧业生产情况数据异常："+e.getMessage());
		}
		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void totalParent(List<Map> list,Map nodeMap,Integer parent_id) {
		for (Map map : list) {
			if(((Integer) map.get("fgid")) == parent_id) {
				map.put("surplus_size", 
						new BigDecimal(null==map.get("surplus_size")?"0":map.get("surplus_size").toString())
						.add(new BigDecimal(null==nodeMap.get("surplus_size")?"0":nodeMap.get("surplus_size").toString())));
				map.put("female_size", 
						new BigDecimal(null==map.get("female_size")?"0":map.get("female_size").toString())
						.add(new BigDecimal(null==nodeMap.get("female_size")?"0":nodeMap.get("female_size").toString())));
				map.put("child_size", 
						new BigDecimal(null==map.get("child_size")?"0":map.get("child_size").toString())
						.add(new BigDecimal(null==nodeMap.get("child_size")?"0":nodeMap.get("child_size").toString())));
				map.put("survival_size", 
						new BigDecimal(null==map.get("survival_size")?"0":map.get("survival_size").toString())
						.add(new BigDecimal(null==nodeMap.get("survival_size")?"0":nodeMap.get("survival_size").toString())));
				map.put("death_size", 
						new BigDecimal(null==map.get("death_size")?"0":map.get("death_size").toString())
						.add(new BigDecimal(null==nodeMap.get("death_size")?"0":nodeMap.get("death_size").toString())));
				map.put("maturity_size", 
						new BigDecimal(null==map.get("maturity_size")?"0":map.get("maturity_size").toString())
						.add(new BigDecimal(null==nodeMap.get("maturity_size")?"0":nodeMap.get("maturity_size").toString())));
				map.put("sell_size", 
						new BigDecimal(null==map.get("sell_size")?"0":map.get("sell_size").toString())
						.add(new BigDecimal(null==nodeMap.get("sell_size")?"0":nodeMap.get("sell_size").toString())));
				map.put("meat_output", 
						new BigDecimal(null==map.get("meat_output")?"0":map.get("meat_output").toString())
						.add(new BigDecimal(null==nodeMap.get("meat_output")?"0":nodeMap.get("meat_output").toString())));
				map.put("milk_output", 
						new BigDecimal(null==map.get("milk_output")?"0":map.get("milk_output").toString())
						.add(new BigDecimal(null==nodeMap.get("milk_output")?"0":nodeMap.get("milk_output").toString())));
				map.put("egg_output", 
						new BigDecimal(null==map.get("egg_output")?"0":map.get("egg_output").toString())
						.add(new BigDecimal(null==nodeMap.get("egg_output")?"0":nodeMap.get("egg_output").toString())));
				map.put("hair_output", 
						new BigDecimal(null==map.get("hair_output")?"0":map.get("hair_output").toString())
						.add(new BigDecimal(null==nodeMap.get("hair_output")?"0":nodeMap.get("hair_output").toString())));
				if(((Integer) map.get("parent_id"))!=0) {
					totalParent(list, nodeMap, (Integer) map.get("parent_id"));
				}
			}
		}
	}
	
	public List<Map> findAllForPage(AnimalsBreedQueryVO queryVO, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("查询所有畜牧业生产情况数据开始：" + queryVO);
		}
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询畜牧业生产情况信息，"+queryVO);
		List<Map> list = animalsBreedMapper.findAllForPage(queryVO);
		if(null != list && list.size() > 0) {
			for (Map map : list) {
				//如果是叶子节点，将本节点数据合计到父节点中
				if(((Integer) map.get("isleaf"))==1) {
					totalParent(list, map, (Integer) map.get("parent_id"));
				}
			}
		}
		return list;
	}
	
	public int findAllCount(AnimalsBreedQueryVO queryVO) {
		return animalsBreedMapper.findAllCount(queryVO);
	}
	
	public Map dataImport(MultipartFile file, HttpServletRequest request) {
		if (logger.isInfoEnabled()) {
			logger.info("畜牧业生产情况数据导入开始");
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
				logger.info("畜牧业生产情况数据导入，文件名：" + filename);
			}
	        if (prefix.equals("xlsx")) {
	        	wb = new XSSFWorkbook(file.getInputStream());
	        } else if (prefix.equals("xls")) {
	        	wb = new HSSFWorkbook(file.getInputStream());
	        } else {
	        	if (logger.isInfoEnabled()) {
					logger.info("畜牧业生产情况数据导入，不支持的文件类型");
				}
	        	result.put("code", -1);
	    		result.put("msg", "不支持的文件类型");
	    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(),"不支持的文件类型");
	    		return result;
	        }
	        
	        Sheet sheet1 = wb.getSheetAt(0);
	        int i = 0;
	        String towns = null;
	        Integer ym = null;
	        List<AnimalsBreed> list = new ArrayList<>();
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
	        		   result.put("msg", "报表年月不符合格式，请按照201908填写");
	        		   return result;
	        	   }
	        	   ymstr=ymstr.trim();
	        	    try {
	        	    	ym=Integer.parseInt(ymstr);
					} catch (Exception e) {
						result.put("code", -1);
		        		result.put("msg", "报表年月不符合格式，请按照201908填写");
		        		return result;
					}
	           }
	           if (i >= 6) {
	        	   AnimalsBreed animalsBreed = new AnimalsBreed();
	        	   String name = row.getCell(0).getStringCellValue();
	        	   name=name.trim();
	        	   if(StringUtils.isEmpty(name)) {
	        		   break;
	        	   }
	        	   Map zbmap = getAnimalsTarget(name);
	        	   if(null == zbmap) {
	        		   result.put("code", -1);
	        		   result.put("msg", "第"+(i+1)+"行畜牧业指标在系统中未维护");
	        		   return result;
	        	   }
	        	   animalsBreed.setAnimals_target((Integer) zbmap.get("gid"));
	        	   animalsBreed.setDate_month(ym);
	        	   animalsBreed.setCreator(user.getUser_id());
	        	   animalsBreed.setModifier(user.getUser_id());
	        	   animalsBreed.setCounty("刚察县");
	        	   animalsBreed.setTowns(towns);
	        	   //非叶子节点数据不解析
	        	   if (((Integer) zbmap.get("isleaf"))!=0) {
	        		   animalsBreed.setSurplus_size(row.getCell(1).getNumericCellValue());
		        	   animalsBreed.setFemale_size(row.getCell(2).getNumericCellValue());
		        	   animalsBreed.setChild_size(row.getCell(3).getNumericCellValue());
		        	   animalsBreed.setSurvival_size(row.getCell(4).getNumericCellValue());
		        	   animalsBreed.setDeath_size(row.getCell(5).getNumericCellValue());
		        	   animalsBreed.setMaturity_size(row.getCell(6).getNumericCellValue());
		        	   animalsBreed.setSell_size(row.getCell(7).getNumericCellValue());
		        	   animalsBreed.setMeat_output(row.getCell(8).getNumericCellValue());
		        	   animalsBreed.setMilk_output(row.getCell(9).getNumericCellValue());
		        	   animalsBreed.setEgg_output(row.getCell(10).getNumericCellValue());
		        	   animalsBreed.setHair_output(row.getCell(11).getNumericCellValue());
	        	   }
	        	   list.add(animalsBreed);
	           }
	           i++;
	        }
	        Map m = new HashMap<>();
	        m.put("towns", towns);
	        m.put("date_month", ym);
	        animalsBreedMapper.deleteByTowns(m);
	        animalsBreedMapper.batchInsert(list);
	        LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.SUCESS, user.getUser_id(), "导入查询畜牧业生产情况信息，共导入"+list.size()+"条");
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("畜牧业生产情况数据导入，解析文件异常", e);
			}
			result.put("code", -1);
    		result.put("msg", "解析文件失败");
    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "导入畜牧业生产情况信息异常："+e.getMessage());
		}
		return result;
	}
	
	private void setRowVal(HSSFSheet sheet, List<Node> nodelist, String sp) {
		for (Node node : nodelist) {
			rownum_local.set(rownum_local.get()+1);
			sheet.getRow(rownum_local.get()).getCell(0).setCellValue(sp+node.getName());
			if(null != node.getChildren() && node.getChildren().size() > 0) {
				setRowVal(sheet, node.getChildren(),sp+"   ");
			}
		}
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
			FileUtils.copyFile(ResourceUtils.getFile(staticDir+"/excel/animalsbreed.xls"),destFile);
			is = new FileInputStream(destFile);
			POIFSFileSystem pfs = new POIFSFileSystem(is);
			//读取excel模板
			HSSFWorkbook wb = new HSSFWorkbook(pfs);
			HSSFSheet sheet = wb.getSheetAt(0);
			//获取指标数据
			List<Map> list = (List<Map>) CacheUtil.getCache(CacheTypeEnum.ANIMALSTARGET);
			List<Node> nodelist = new ArrayList<>();
			if (null != list && list.size() > 0) {
				for (Map map : list) {
					nodelist.add(new Node((Integer) map.get("gid"), (Integer) map.get("parent_id"), (String) map.get("target_name")));
				}
				rownum_local.set(5);
				setRowVal(sheet, TreeBuilder.buildListToTree(nodelist), "");
				rownum_local.remove();
				//强制下载不打开
	    		response.setContentType("application/octet-stream");
	            //使用URLEncoder来防止文件名乱码或者读取错误
	            response.setHeader("Content-Disposition", "attachment; filename="+URLEncoder.encode("畜牧业生产月报表.xls", "UTF-8"));
	            wb.write(out);
			}else {
				out.write(new String("系统未维护畜牧业指标信息，请联系管理员新增").getBytes("utf-8"));
			}
            
        } catch (Exception e) {
            logger.error("生成模板文件异常",e);
            try {
				out.write(new String("系统未维护畜牧业指标信息，请联系管理员新增").getBytes("utf-8"));
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
	
	private Map getAnimalsTarget(String name) {
		if (StringUtils.isEmpty(name)) {
			return null;
		}
		List<Map> list = (List<Map>) CacheUtil.getCache(CacheTypeEnum.ANIMALSTARGET);
		if (list == null || list.size() == 0) {
			return null;
		}
		for (Map map : list) {
			if(name.equals((String) map.get("target_name"))) {
				return map;
			}
		}
		return null;
	}
}
