package com.agri.monitor.service.datamanage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.agri.monitor.entity.AnimalsBreed;
import com.agri.monitor.entity.FishInfo;
import com.agri.monitor.entity.GrassInfo;
import com.agri.monitor.entity.MonitorLog;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.CacheTypeEnum;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.AgriInputinfoMapper;
import com.agri.monitor.mapper.AnimalsBreedMapper;
import com.agri.monitor.mapper.FarmProductInfoMapper;
import com.agri.monitor.mapper.GrassInfoMapper;
import com.agri.monitor.mapper.LandResourceMapper;
import com.agri.monitor.mapper.MonitorLogMapper;
import com.agri.monitor.mapper.NmshInfoMapper;
import com.agri.monitor.utils.CacheUtil;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.utils.TreeBuilder;
import com.agri.monitor.vo.AgriInputinfoQueryVO;
import com.agri.monitor.vo.AnimalsBreedQueryVO;
import com.agri.monitor.vo.FarmProductQueryVO;
import com.agri.monitor.vo.GrassQueryVO;
import com.agri.monitor.vo.LandResourceQueryVO;
import com.agri.monitor.vo.NmshInfoQueryVO;
import com.agri.monitor.vo.Node;

@Service
public class AnimalsBreedService {
	
	private static final Logger logger = LoggerFactory.getLogger(AnimalsBreedService.class);
	
	private static ThreadLocal<Integer> rownum_local = new ThreadLocal<>(); 
	@Value("${temp.excel.dir}")
	private String tempdir;
	@Autowired
	private AnimalsBreedMapper animalsBreedMapper;
	@Autowired
	private LandResourceMapper landResourceMapper;
	@Autowired
	private GrassInfoMapper grassInfoMapper;
	@Autowired
	private FarmProductInfoMapper productInfoMapper;
	@Autowired
	private MonitorLogMapper monitorLogMapper;
	@Autowired
	private AgriInputinfoMapper agriInputinfoMapper;
	@Autowired
	private NmshInfoMapper nmshInfoMapper;
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
	        //String towns = null;
	        Integer ym = null;
	        List<AnimalsBreed> list = new ArrayList<>();
	        for (Row row : sheet1) {
	        	//解析所属乡镇
				/*
				 * if (i == 1) { towns = row.getCell(1).getStringCellValue();
				 * if(StringUtils.isEmpty(towns)) { result.put("code", -1); result.put("msg",
				 * "乡镇未填写"); return result; } }
				 */
	           if(i == 1) {
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
	           if (i >= 5) {
	        	   AnimalsBreed animalsBreed = new AnimalsBreed();
	        	   String name = row.getCell(0).getStringCellValue();
	        	   if(StringUtils.isEmpty(name)) {
	        		   break;
	        	   }
	        	   name=name.trim();
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
	        	   //animalsBreed.setTowns(towns);
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
	        //m.put("towns", towns);
	        m.put("date_month", ym);
	        animalsBreedMapper.deleteByTowns(m);
	        animalsBreedMapper.batchInsert(list);
	        LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.SUCESS, user.getUser_id(), "导入查询畜牧业生产情况信息，共导入"+list.size()+"条");
		} catch (Exception e) {
			logger.error("畜牧业生产情况数据导入，解析文件异常", e);
			result.put("code", -1);
    		result.put("msg", "解析文件失败");
    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "导入畜牧业生产情况信息异常："+e.getMessage());
		}
		return result;
	}
	
	//模板下载
	public void downloadFile(HttpServletResponse response) {
		File destFile = null;
		InputStream is = null;
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			//复制原模板到临时文件中
			destFile = new File(tempdir+new Date().getTime()+".xls");
			FileUtils.copyFile(ResourceUtils.getFile(tempdir+"/animalsbreed.xls"),destFile);
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
					if(1 == (Integer) map.get("stopflag")) {
						nodelist.add(new Node((Integer) map.get("gid"), (Integer) map.get("parent_id"), (String) map.get("target_name")));
					}
				}
				rownum_local.set(4);
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
	
	public List<Map> getMonthData(Integer date_month, String userid){
		if (logger.isErrorEnabled()) {
			logger.info("获取畜牧业生产情况月度数据，month="+date_month);
		}
		
		AnimalsBreedQueryVO queryvo = new AnimalsBreedQueryVO();
		queryvo.setPage(1);
		queryvo.setLimit(Integer.MAX_VALUE);
		queryvo.setStopflag(1);
		
		queryvo.setDate_month(date_month);
		//查询当月数据
		List<Map> dylist=animalsBreedMapper.findAllForPage(queryvo);
		
		if(null != dylist && dylist.size() > 0) {
			//查询上年年末数据
			queryvo.setDate_month(
					Integer.valueOf(((date_month-100)+"").substring(0, 4) +"12"));
			List<Map> nmlist=animalsBreedMapper.findAllForPage(queryvo);
			if (logger.isErrorEnabled()) {
				logger.info("获取畜牧业生产情况上年年末数据完成");
			}
			Map<String, Map> nmflData = new HashMap<>();
			//统计父节点合计数
			if(null != nmlist && nmlist.size() > 0) {
				for (Map map : nmlist) {
					//如果是叶子节点，将本节点数据合计到父节点中
					if(((Integer) map.get("isleaf"))==1) {
						totalParent(nmlist, map, (Integer) map.get("parent_id"));
					}
				}
				//按分类存储
				for (Map map : nmlist) {
					nmflData.put(map.get("fgid").toString(), map);
				}
			}
			
			//查询去年同期数据
			queryvo.setDate_month(date_month-100);
			List<Map> tqlist=animalsBreedMapper.findAllForPage(queryvo);
			if (logger.isErrorEnabled()) {
				logger.info("获取畜牧业生产情况月度数据和上年同期数据完成");
			}
			Map<String, Map> tqflData = new HashMap<>();
			if(null != tqlist && tqlist.size() > 0) {
				for (Map map : tqlist) {
					//如果是叶子节点，将本节点数据合计到父节点中
					if(((Integer) map.get("isleaf"))==1) {
						totalParent(tqlist, map, (Integer) map.get("parent_id"));
					}
				}
				//按分类存储
				for (Map map : tqlist) {
					tqflData.put(map.get("fgid").toString(), map);
				}
			}
			
			for (Map map : dylist) {
				//如果是叶子节点，将本节点数据合计到父节点中
				if(((Integer) map.get("isleaf"))==1) {
					totalParent(dylist, map, (Integer) map.get("parent_id"));
				}
			}
			for (Map map : dylist) {
				Map nmmap = nmflData.get(map.get("fgid").toString());
				Map tqmap = tqflData.get(map.get("fgid").toString());
				map.put("nccl", null!=nmmap?nmmap.get("surplus_size"):null);
				map.put("czs_tq", null!=tqmap?tqmap.get("child_size"):null);
				map.put("chs_tq", null!=tqmap?tqmap.get("survival_size"):null);
				//存活率
				BigDecimal survival_size = null!=map.get("survival_size")?new BigDecimal(map.get("survival_size").toString()):null;
				BigDecimal child_size = null!=map.get("child_size")?new BigDecimal(map.get("child_size").toString()):null;
				if(null != survival_size && null != child_size && child_size.doubleValue()!=0) {
					map.put("chl", survival_size.divide(child_size,5,RoundingMode.HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_DOWN));
				}
				BigDecimal czs_tq = null!=map.get("czs_tq")?new BigDecimal(map.get("czs_tq").toString()):null;
				BigDecimal chs_tq= null!=map.get("chs_tq")?new BigDecimal(map.get("chs_tq").toString()):null;
				if(null != chs_tq && null != czs_tq && czs_tq.doubleValue()!=0) {
					map.put("chl_tq", chs_tq.divide(czs_tq,5,RoundingMode.HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_DOWN));
				}
				//损亡情况
				map.put("sws_tq", null!=tqmap?tqmap.get("death_size"):null);
				BigDecimal death_size = null!=map.get("death_size")?new BigDecimal(map.get("death_size").toString()):null;
				//损亡率
				if(null != death_size && null != child_size && child_size.doubleValue()!=0) {
					map.put("swl", death_size.divide(child_size,5,RoundingMode.HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_DOWN));
				}
				BigDecimal sws_tq= null!=map.get("sws_tq")?new BigDecimal(map.get("sws_tq").toString()):null;
				if(null != sws_tq && null != czs_tq && czs_tq.doubleValue()!=0) {
					map.put("swl_tq", sws_tq.divide(czs_tq,5,RoundingMode.HALF_UP).setScale(5, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_DOWN));
				}
			}
		}
		
		if (logger.isErrorEnabled()) {
			logger.info("子节点数据汇总到父节点完成");
		}
		
		return dylist;
	}
	
	public List<Map> getYearData(Integer year, String userid){
		if (logger.isErrorEnabled()) {
			logger.info("获取畜牧业生产情况年度数据，year="+year);
		}
		AnimalsBreedQueryVO queryvo = new AnimalsBreedQueryVO();
		queryvo.setDate_month(Integer.valueOf((year-1)+"12"));
		queryvo.setPage(1);
		queryvo.setLimit(Integer.MAX_VALUE);
		queryvo.setStopflag(1);
		//查询上年年末数据
		List<Map> list=animalsBreedMapper.findAllForPage(queryvo);
		if (logger.isErrorEnabled()) {
			logger.info("获取畜牧业生产情况上年年末数据完成");
		}
		Map<String, Map> flData = new HashMap<>();
		//统计父节点合计数
		if(null != list && list.size() > 0) {
			for (Map map : list) {
				//如果是叶子节点，将本节点数据合计到父节点中
				if(((Integer) map.get("isleaf"))==1) {
					totalParent(list, map, (Integer) map.get("parent_id"));
				}
			}
			//按分类存储
			for (Map map : list) {
				flData.put(map.get("fgid").toString(), map);
			}
		}
		
		Map m = new HashMap<>();
		m.put("m1", Integer.valueOf(year+"01"));
		m.put("m2", Integer.valueOf(year+"12"));
		List<Map> list1 = animalsBreedMapper.getYearData(m);
		if (logger.isErrorEnabled()) {
			logger.info("获取畜牧业生产情况本年数据完成");
		}
		if(null != list1 && list1.size() > 0) {
			for (Map map : list1) {
				//如果是叶子节点，将本节点数据合计到父节点中
				if(((Integer) map.get("isleaf"))==1) {
					totalParent(list1, map, (Integer) map.get("parent_id"));
				}
			}
			for (Map map : list1) {
				if(null != flData.get(map.get("fgid").toString())) {
					map.put("nccl", flData.get(map.get("fgid").toString()).get("surplus_size"));
					map.put("nfmc", flData.get(map.get("fgid").toString()).get("female_size"));
				}
			}
		}
		if (userid!=null) {
			LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "畜牧业生产情况年度数据分析");
		}
		return list1;
	}
	/**
	 * 导出年报表
	 * @param response
	 */
	public void exportYearData(HttpServletResponse response, Integer year, String userid) {
		File destFile = null;
		InputStream is = null;
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			//复制原模板到临时文件中
			destFile = new File(tempdir+new Date().getTime()+".xls");
			FileUtils.copyFile(ResourceUtils.getFile(tempdir+"/year_export.xls"),destFile);
			is = new FileInputStream(destFile);
			POIFSFileSystem pfs = new POIFSFileSystem(is);
			//读取excel模板
			HSSFWorkbook wb = new HSSFWorkbook(pfs);
			HSSFSheet sheet = wb.getSheetAt(0);
			//获取指标数据
			List<Map> list = getYearData(year, userid);
			List<Node> nodelist = new ArrayList<>();
			if (null != list && list.size() > 0) {
				for (Map map : list) {
					nodelist.add(new Node((Integer) map.get("fgid"), (Integer) map.get("parent_id"), map));
				}
				sheet.getRow(0).getCell(0).setCellValue(year+"年畜牧业生产情况");
				rownum_local.set(4);
				setRowValForYearData(sheet, TreeBuilder.buildListToTree(nodelist), "");
				rownum_local.remove();
				//强制下载不打开
	    		response.setContentType("application/octet-stream");
	            //使用URLEncoder来防止文件名乱码或者读取错误
	            response.setHeader("Content-Disposition", "attachment; filename="+URLEncoder.encode(year+"年畜牧业生产情况.xls", "UTF-8"));
	            wb.write(out);
			} else {
				out.write(new String("没有畜牧业生产情况年度数据，请先维护。").getBytes("utf-8"));
			}
        } catch (Exception e) {
            logger.error("生成模板文件异常",e);
            try {
				out.write(new String("导出数据失败").getBytes("utf-8"));
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
	/**
	 * 导出月报表
	 * @param response
	 */
	public void exportMonthData(HttpServletResponse response, Integer month, String userid) {
		File destFile = null;
		InputStream is = null;
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			//复制原模板到临时文件中
			destFile = new File(tempdir+new Date().getTime()+".xls");
			FileUtils.copyFile(ResourceUtils.getFile(tempdir+"/month_export.xls"),destFile);
			is = new FileInputStream(destFile);
			POIFSFileSystem pfs = new POIFSFileSystem(is);
			//读取excel模板
			HSSFWorkbook wb = new HSSFWorkbook(pfs);
			HSSFSheet sheet = wb.getSheetAt(0);
			//获取指标数据
			List<Map> list = getMonthData(month, userid);
			List<Node> nodelist = new ArrayList<>();
			if (null != list && list.size() > 0) {
				for (Map map : list) {
					nodelist.add(new Node((Integer) map.get("fgid"), (Integer) map.get("parent_id"), map));
				}
				sheet.getRow(0).getCell(0).setCellValue(month+"畜牧业生产月报表");
				rownum_local.set(4);
				setRowValForMonthData(sheet, TreeBuilder.buildListToTree(nodelist), "");
				rownum_local.remove();
				//强制下载不打开
	    		response.setContentType("application/octet-stream");
	            //使用URLEncoder来防止文件名乱码或者读取错误
	            response.setHeader("Content-Disposition", "attachment; filename="+URLEncoder.encode(month+"畜牧业生产月报表.xls", "UTF-8"));
	            wb.write(out);
			} else {
				out.write(new String("没有畜牧业生产情况月度数据，请先维护。").getBytes("utf-8"));
			}
        } catch (Exception e) {
            logger.error("生成模板文件异常",e);
            try {
				out.write(new String("导出数据失败").getBytes("utf-8"));
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
	
	/**
	 * 导出考核指标表
	 * @param response
	 */
	public void exportKHZB(HttpServletResponse response, Integer syear, Integer eyear) {
		File destFile = null;
		InputStream is = null;
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			//复制原模板到临时文件中
			destFile = new File(tempdir+new Date().getTime()+".xls");
			FileUtils.copyFile(ResourceUtils.getFile(tempdir+"/khzb_export.xls"),destFile);
			is = new FileInputStream(destFile);
			POIFSFileSystem pfs = new POIFSFileSystem(is);
			//读取excel模板
			HSSFWorkbook wb = new HSSFWorkbook(pfs);
			HSSFSheet sheet = wb.getSheetAt(0);
			
			//种植业数据
			Map<String, Map> zzydata = getzzydata(syear, eyear);
			//草原生态数据
			Map<String, GrassInfo> cystdata = getcystdata(syear, eyear);
			//获取畜牧业生产数据
			List<Map> cmydata1 = getYearData(syear, null);
			List<Map> cmydata2 = getYearData(syear + 1, null);
			List<Map> cmydata3 = getYearData(eyear, null);
			//农产品信息数据
			Map<String, FishInfo> ncpdata = getncpdata(syear, eyear);
			//农业投入数据
			Map<String, Map> nytrdata = getnytrdata(syear, eyear);
			//农民生活数据
			Map<String, Map> nmshdata = getnmshdata(syear, eyear);
			
			HSSFRow row2 = sheet.getRow(2);
			row2.getCell(5).setCellValue(syear+"年");
			row2.getCell(6).setCellValue((syear+1)+"年");
			row2.getCell(7).setCellValue(eyear+"年");
			//耕地面积
			HSSFRow row3 = sheet.getRow(3);
			if(zzydata.get(syear+"")!=null) {
				row3.getCell(5).setCellValue(objToDou(zzydata.get(syear+"").get("gdmj"))/1000);
			}
			if(zzydata.get((syear+1)+"")!=null) {
				row3.getCell(6).setCellValue(objToDou(zzydata.get((syear+1)+"").get("gdmj"))/1000);
			}
			if(zzydata.get(eyear+"")!=null) {
				row3.getCell(7).setCellValue(objToDou(zzydata.get(eyear+"").get("gdmj"))/1000);
			}
			//高标准农田面积
			HSSFRow row4 = sheet.getRow(4);
			if(zzydata.get(syear+"")!=null) {
				row4.getCell(5).setCellValue(objToDou(zzydata.get(syear+"").get("gbzltmj"))/1000);
			}
			if(zzydata.get((syear+1)+"")!=null) {
				row4.getCell(6).setCellValue(objToDou(zzydata.get((syear+1)+"").get("gbzltmj"))/1000);
			}
			if(zzydata.get(eyear+"")!=null) {
				row4.getCell(7).setCellValue(objToDou(zzydata.get(eyear+"").get("gbzltmj"))/1000);
			}
			//土壤有机质含量
			HSSFRow row5 = sheet.getRow(5);
			if(zzydata.get(syear+"")!=null) {
				row5.getCell(5).setCellValue(objToDou(zzydata.get(syear+"").get("yjz")));
			}
			if(zzydata.get((syear+1)+"")!=null) {
				row5.getCell(6).setCellValue(objToDou(zzydata.get((syear+1)+"").get("yjz")));
			}
			if(zzydata.get(eyear+"")!=null) {
				row5.getCell(7).setCellValue(objToDou(zzydata.get(eyear+"").get("yjz")));
			}
			//播种面积
			HSSFRow row7 = sheet.getRow(7);
			if(zzydata.get(syear+"")!=null) {
				row7.getCell(5).setCellValue(objToDou(zzydata.get(syear+"").get("qkmj"))/1000+objToDou(zzydata.get(syear+"").get("ycmj"))/1000+objToDou(zzydata.get(syear+"").get("ymmj"))/1000);
			}
			if(zzydata.get((syear+1)+"")!=null) {
				row7.getCell(6).setCellValue(objToDou(zzydata.get((syear+1)+"").get("qkmj"))/1000+objToDou(zzydata.get((syear+1)+"").get("ycmj"))/1000+objToDou(zzydata.get((syear+1)+"").get("ymmj"))/1000);
			}
			if(zzydata.get(eyear+"")!=null && zzydata.get(eyear+"").get("yjz")!=null) {
				row7.getCell(7).setCellValue(objToDou(zzydata.get(eyear+"").get("qkmj"))/1000+objToDou(zzydata.get(eyear+"").get("ycmj"))/1000+objToDou(zzydata.get(eyear+"").get("ymmj"))/1000);
			}
			//多年生人工草地保留面积
			HSSFRow row8 = sheet.getRow(8);
			if(cystdata.get(syear+"")!=null) {
				row8.getCell(5).setCellValue(cystdata.get(syear+"").getGrass_retain_area());
			}
			if(cystdata.get((syear+1)+"")!=null) {
				row8.getCell(6).setCellValue(cystdata.get((syear+1)+"").getGrass_retain_area());
			}
			if(cystdata.get(eyear+"")!=null) {
				row8.getCell(7).setCellValue(cystdata.get(eyear+"").getGrass_retain_area());
			}
			//生猪出栏量
			HSSFRow row9 = sheet.getRow(9);
			if(cmydata1!=null && cmydata1.size() > 0) {
				for (Map map : cmydata1) {
					if(ObjToStr(map.get("target_name")).equals("猪")) {
						row9.getCell(5).setCellValue(objToDou(map.get("maturity_size")));
						break;
					}
				}
			}
			if(cmydata2!=null && cmydata2.size() > 0) {
				for (Map map : cmydata2) {
					if(ObjToStr(map.get("target_name")).equals("猪")) {
						row9.getCell(6).setCellValue(objToDou(map.get("maturity_size")));
						break;
					}
				}
			}
			if(cmydata3!=null && cmydata3.size() > 0) {
				for (Map map : cmydata3) {
					if(ObjToStr(map.get("target_name")).equals("猪")) {
						row9.getCell(7).setCellValue(objToDou(map.get("maturity_size")));
						break;
					}
				}
			}
			//肉牛出栏量
			HSSFRow row10 = sheet.getRow(10);
			if(cmydata1!=null && cmydata1.size() > 0) {
				for (Map map : cmydata1) {
					if(ObjToStr(map.get("target_name")).equals("牛")) {
						row10.getCell(5).setCellValue(objToDou(map.get("maturity_size")));
						break;
					}
				}
			}
			if(cmydata2!=null && cmydata2.size() > 0) {
				for (Map map : cmydata2) {
					if(ObjToStr(map.get("target_name")).equals("牛")) {
						row10.getCell(6).setCellValue(objToDou(map.get("maturity_size")));
						break;
					}
				}
			}
			if(cmydata3!=null && cmydata3.size() > 0) {
				for (Map map : cmydata3) {
					if(ObjToStr(map.get("target_name")).equals("牛")) {
						row10.getCell(7).setCellValue(objToDou(map.get("maturity_size")));
						break;
					}
				}
			}
			//奶牛存栏量
			HSSFRow row11 = sheet.getRow(11);
			if(cmydata1!=null && cmydata1.size() > 0) {
				for (Map map : cmydata1) {
					if(ObjToStr(map.get("target_name")).equals("奶牛")) {
						row11.getCell(5).setCellValue(objToDou(map.get("nccl")));
						break;
					}
				}
			}
			if(cmydata2!=null && cmydata2.size() > 0) {
				for (Map map : cmydata2) {
					if(ObjToStr(map.get("target_name")).equals("奶牛")) {
						row11.getCell(6).setCellValue(objToDou(map.get("nccl")));
						break;
					}
				}
			}
			if(cmydata3!=null && cmydata3.size() > 0) {
				for (Map map : cmydata3) {
					if(ObjToStr(map.get("target_name")).equals("奶牛")) {
						row11.getCell(7).setCellValue(objToDou(map.get("nccl")));
						break;
					}
				}
			}
			//肉羊出栏量
			HSSFRow row12 = sheet.getRow(12);
			if(cmydata1!=null && cmydata1.size() > 0) {
				for (Map map : cmydata1) {
					if(ObjToStr(map.get("target_name")).equals("羊")) {
						row12.getCell(5).setCellValue(objToDou(map.get("maturity_size")));
						break;
					}
				}
			}
			if(cmydata2!=null && cmydata2.size() > 0) {
				for (Map map : cmydata2) {
					if(ObjToStr(map.get("target_name")).equals("羊")) {
						row12.getCell(6).setCellValue(objToDou(map.get("maturity_size")));
						break;
					}
				}
			}
			if(cmydata3!=null && cmydata3.size() > 0) {
				for (Map map : cmydata3) {
					if(ObjToStr(map.get("target_name")).equals("羊")) {
						row12.getCell(7).setCellValue(objToDou(map.get("maturity_size")));
						break;
					}
				}
			}
			//肉禽出栏量
			HSSFRow row13 = sheet.getRow(13);
			if(cmydata1!=null && cmydata1.size() > 0) {
				for (Map map : cmydata1) {
					if(ObjToStr(map.get("target_name")).equals("家禽")) {
						row13.getCell(5).setCellValue(objToDou(map.get("maturity_size")));
						break;
					}
				}
			}
			if(cmydata2!=null && cmydata2.size() > 0) {
				for (Map map : cmydata2) {
					if(ObjToStr(map.get("target_name")).equals("家禽")) {
						row13.getCell(6).setCellValue(objToDou(map.get("maturity_size")));
						break;
					}
				}
			}
			if(cmydata3!=null && cmydata3.size() > 0) {
				for (Map map : cmydata3) {
					if(ObjToStr(map.get("target_name")).equals("家禽")) {
						row13.getCell(7).setCellValue(objToDou(map.get("maturity_size")));
						break;
					}
				}
			}
			//奶牛存栏量
			HSSFRow row14 = sheet.getRow(14);
			if(cmydata1!=null && cmydata1.size() > 0) {
				for (Map map : cmydata1) {
					if(ObjToStr(map.get("target_name")).equals("专用型蛋鸡")) {
						row14.getCell(5).setCellValue(objToDou(map.get("nccl")));
						break;
					}
				}
			}
			if(cmydata2!=null && cmydata2.size() > 0) {
				for (Map map : cmydata2) {
					if(ObjToStr(map.get("target_name")).equals("专用型蛋鸡")) {
						row14.getCell(6).setCellValue(objToDou(map.get("nccl")));
						break;
					}
				}
			}
			if(cmydata3!=null && cmydata3.size() > 0) {
				for (Map map : cmydata3) {
					if(ObjToStr(map.get("target_name")).equals("专用型蛋鸡")) {
						row14.getCell(7).setCellValue(objToDou(map.get("nccl")));
						break;
					}
				}
			}
			//猪肉产量
			HSSFRow row22 = sheet.getRow(22);
			if(cmydata1!=null && cmydata1.size() > 0) {
				for (Map map : cmydata1) {
					if(ObjToStr(map.get("target_name")).equals("猪")) {
						row22.getCell(5).setCellValue(objToDou(map.get("meat_output")));
						break;
					}
				}
			}
			if(cmydata2!=null && cmydata2.size() > 0) {
				for (Map map : cmydata2) {
					if(ObjToStr(map.get("target_name")).equals("猪")) {
						row22.getCell(6).setCellValue(objToDou(map.get("meat_output")));
						break;
					}
				}
			}
			if(cmydata3!=null && cmydata3.size() > 0) {
				for (Map map : cmydata3) {
					if(ObjToStr(map.get("target_name")).equals("猪")) {
						row22.getCell(7).setCellValue(objToDou(map.get("meat_output")));
						break;
					}
				}
			}
			//牛肉产量
			HSSFRow row23 = sheet.getRow(23);
			if(cmydata1!=null && cmydata1.size() > 0) {
				for (Map map : cmydata1) {
					if(ObjToStr(map.get("target_name")).equals("牛")) {
						row23.getCell(5).setCellValue(objToDou(map.get("meat_output")));
						break;
					}
				}
			}
			if(cmydata2!=null && cmydata2.size() > 0) {
				for (Map map : cmydata2) {
					if(ObjToStr(map.get("target_name")).equals("牛")) {
						row23.getCell(6).setCellValue(objToDou(map.get("meat_output")));
						break;
					}
				}
			}
			if(cmydata3!=null && cmydata3.size() > 0) {
				for (Map map : cmydata3) {
					if(ObjToStr(map.get("target_name")).equals("牛")) {
						row23.getCell(7).setCellValue(objToDou(map.get("meat_output")));
						break;
					}
				}
			}
			//羊肉产量
			HSSFRow row24 = sheet.getRow(24);
			if(cmydata1!=null && cmydata1.size() > 0) {
				for (Map map : cmydata1) {
					if(ObjToStr(map.get("target_name")).equals("羊")) {
						row24.getCell(5).setCellValue(objToDou(map.get("meat_output")));
						break;
					}
				}
			}
			if(cmydata2!=null && cmydata2.size() > 0) {
				for (Map map : cmydata2) {
					if(ObjToStr(map.get("target_name")).equals("羊")) {
						row24.getCell(6).setCellValue(objToDou(map.get("meat_output")));
						break;
					}
				}
			}
			if(cmydata3!=null && cmydata3.size() > 0) {
				for (Map map : cmydata3) {
					if(ObjToStr(map.get("target_name")).equals("羊")) {
						row24.getCell(7).setCellValue(objToDou(map.get("meat_output")));
						break;
					}
				}
			}
			//牛奶
			HSSFRow row25 = sheet.getRow(25);
			if(cmydata1!=null && cmydata1.size() > 0) {
				for (Map map : cmydata1) {
					if(ObjToStr(map.get("target_name")).equals("牛")) {
						row25.getCell(5).setCellValue(objToDou(map.get("milk_output")));
						break;
					}
				}
			}
			if(cmydata2!=null && cmydata2.size() > 0) {
				for (Map map : cmydata2) {
					if(ObjToStr(map.get("target_name")).equals("牛")) {
						row25.getCell(6).setCellValue(objToDou(map.get("milk_output")));
						break;
					}
				}
			}
			if(cmydata3!=null && cmydata3.size() > 0) {
				for (Map map : cmydata3) {
					if(ObjToStr(map.get("target_name")).equals("牛")) {
						row25.getCell(7).setCellValue(objToDou(map.get("milk_output")));
						break;
					}
				}
			}
			//禽肉
			HSSFRow row26 = sheet.getRow(26);
			if(cmydata1!=null && cmydata1.size() > 0) {
				for (Map map : cmydata1) {
					if(ObjToStr(map.get("target_name")).equals("家禽")) {
						row26.getCell(5).setCellValue(objToDou(map.get("meat_output")));
						break;
					}
				}
			}
			if(cmydata2!=null && cmydata2.size() > 0) {
				for (Map map : cmydata2) {
					if(ObjToStr(map.get("target_name")).equals("家禽")) {
						row26.getCell(6).setCellValue(objToDou(map.get("meat_output")));
						break;
					}
				}
			}
			if(cmydata3!=null && cmydata3.size() > 0) {
				for (Map map : cmydata3) {
					if(ObjToStr(map.get("target_name")).equals("家禽")) {
						row26.getCell(7).setCellValue(objToDou(map.get("meat_output")));
						break;
					}
				}
			}
			//禽蛋
			HSSFRow row27 = sheet.getRow(27);
			if(cmydata1!=null && cmydata1.size() > 0) {
				for (Map map : cmydata1) {
					if(ObjToStr(map.get("target_name")).equals("专用型蛋鸡")) {
						row27.getCell(5).setCellValue(objToDou(map.get("egg_output")));
						break;
					}
				}
			}
			if(cmydata2!=null && cmydata2.size() > 0) {
				for (Map map : cmydata2) {
					if(ObjToStr(map.get("target_name")).equals("专用型蛋鸡")) {
						row27.getCell(6).setCellValue(objToDou(map.get("egg_output")));
						break;
					}
				}
			}
			if(cmydata3!=null && cmydata3.size() > 0) {
				for (Map map : cmydata3) {
					if(ObjToStr(map.get("target_name")).equals("专用型蛋鸡")) {
						row27.getCell(7).setCellValue(objToDou(map.get("egg_output")));
						break;
					}
				}
			}
			//种植业
			HSSFRow row31 = sheet.getRow(31);
			//TODO
			//化肥施用量（折纯量） 
			HSSFRow row37 = sheet.getRow(37);
			if(nytrdata.get(syear+"")!=null) {
				row37.getCell(5).setCellValue(objToDou(nytrdata.get(syear+"").get("n"))+objToDou(nytrdata.get(syear+"").get("p"))+objToDou(nytrdata.get(syear+"").get("yjf")));
			}
			if(nytrdata.get((syear+1)+"")!=null) {
				row37.getCell(6).setCellValue(objToDou(nytrdata.get((syear+1)+"").get("n"))+objToDou(nytrdata.get((syear+1)+"").get("p"))+objToDou(nytrdata.get((syear+1)+"").get("yjf")));
			}
			if(nytrdata.get(eyear+"")!=null) {
				row37.getCell(7).setCellValue(objToDou(nytrdata.get(eyear+"").get("n"))+objToDou(nytrdata.get(eyear+"").get("p"))+objToDou(nytrdata.get(eyear+"").get("yjf")));
			}
			//农药施用量（折百量）
			HSSFRow row38 = sheet.getRow(38);
			if(nytrdata.get(syear+"")!=null) {
				row38.getCell(5).setCellValue(objToDou(nytrdata.get(syear+"").get("lyqd")));
			}
			if(nytrdata.get((syear+1)+"")!=null) {
				row38.getCell(6).setCellValue(objToDou(nytrdata.get((syear+1)+"").get("lyqd")));
			}
			if(nytrdata.get(eyear+"")!=null) {
				row38.getCell(7).setCellValue(objToDou(nytrdata.get(eyear+"").get("lyqd")));
			}
			//综合利用的秸秆量
			HSSFRow row39 = sheet.getRow(39);
			if(nytrdata.get(syear+"")!=null) {
				row39.getCell(5).setCellValue(objToDou(nytrdata.get(syear+"").get("zhlyjg")));
			}
			if(nytrdata.get((syear+1)+"")!=null) {
				row39.getCell(6).setCellValue(objToDou(nytrdata.get((syear+1)+"").get("zhlyjg")));
			}
			if(nytrdata.get(eyear+"")!=null) {
				row39.getCell(7).setCellValue(objToDou(nytrdata.get(eyear+"").get("zhlyjg")));
			}
			//秸秆可收集资源量
			HSSFRow row40 = sheet.getRow(40);
			if(nytrdata.get(syear+"")!=null) {
				row40.getCell(5).setCellValue(objToDou(nytrdata.get(syear+"").get("jgksj")));
			}
			if(nytrdata.get((syear+1)+"")!=null) {
				row40.getCell(6).setCellValue(objToDou(nytrdata.get((syear+1)+"").get("jgksj")));
			}
			if(nytrdata.get(eyear+"")!=null) {
				row40.getCell(7).setCellValue(objToDou(nytrdata.get(eyear+"").get("jgksj")));
			}
			//回收利用的农膜总量
			HSSFRow row41 = sheet.getRow(41);
			if(nytrdata.get(syear+"")!=null) {
				row41.getCell(5).setCellValue(objToDou(nytrdata.get(syear+"").get("lmzl")));
			}
			if(nytrdata.get((syear+1)+"")!=null) {
				row41.getCell(6).setCellValue(objToDou(nytrdata.get((syear+1)+"").get("lmzl")));
			}
			if(nytrdata.get(eyear+"")!=null) {
				row41.getCell(7).setCellValue(objToDou(nytrdata.get(eyear+"").get("lmzl")));
			}
			//农膜使用总量
			HSSFRow row42 = sheet.getRow(42);
			if(nytrdata.get(syear+"")!=null) {
				row42.getCell(5).setCellValue(objToDou(nytrdata.get(syear+"").get("lmsyl")));
			}
			if(nytrdata.get((syear+1)+"")!=null) {
				row42.getCell(6).setCellValue(objToDou(nytrdata.get((syear+1)+"").get("lmsyl")));
			}
			if(nytrdata.get(eyear+"")!=null) {
				row42.getCell(7).setCellValue(objToDou(nytrdata.get(eyear+"").get("lmsyl")));
			}
			//综合利用的畜禽粪污量
			HSSFRow row43 = sheet.getRow(43);
			if(nytrdata.get(syear+"")!=null) {
				row43.getCell(5).setCellValue(objToDou(nytrdata.get(syear+"").get("qcfl")));
			}
			if(nytrdata.get((syear+1)+"")!=null) {
				row43.getCell(6).setCellValue(objToDou(nytrdata.get((syear+1)+"").get("qcfl")));
			}
			if(nytrdata.get(eyear+"")!=null) {
				row43.getCell(7).setCellValue(objToDou(nytrdata.get(eyear+"").get("qcfl")));
			}
			//畜禽粪污总量
			HSSFRow row44 = sheet.getRow(44);
			if(nytrdata.get(syear+"")!=null) {
				row44.getCell(5).setCellValue(objToDou(nytrdata.get(syear+"").get("qcfzl")));
			}
			if(nytrdata.get((syear+1)+"")!=null) {
				row44.getCell(6).setCellValue(objToDou(nytrdata.get((syear+1)+"").get("qcfzl")));
			}
			if(nytrdata.get(eyear+"")!=null) {
				row44.getCell(7).setCellValue(objToDou(nytrdata.get(eyear+"").get("qcfzl")));
			}
			//农村居民人均可支配收入
			HSSFRow row45 = sheet.getRow(45);
			if(nmshdata.get(syear+"")!=null) {
				row45.getCell(5).setCellValue(objToDou(nmshdata.get(syear+"").get("rjsr")));
			}
			if(nmshdata.get((syear+1)+"")!=null) {
				row45.getCell(6).setCellValue(objToDou(nmshdata.get((syear+1)+"").get("rjsr")));
			}
			if(nmshdata.get(eyear+"")!=null) {
				row45.getCell(7).setCellValue(objToDou(nmshdata.get(eyear+"").get("rjsr")));
			}
			//实施生活垃圾集中手机处理的行政村数
			HSSFRow row46 = sheet.getRow(46);
			if(nmshdata.get(syear+"")!=null) {
				row46.getCell(5).setCellValue(objToDou(nmshdata.get(syear+"").get("ljsjxzcs")));
			}
			if(nmshdata.get((syear+1)+"")!=null) {
				row46.getCell(6).setCellValue(objToDou(nmshdata.get((syear+1)+"").get("ljsjxzcs")));
			}
			if(nmshdata.get(eyear+"")!=null) {
				row46.getCell(7).setCellValue(objToDou(nmshdata.get(eyear+"").get("ljsjxzcs")));
			}
			//有生活污水处理设施的行政村数
			HSSFRow row47 = sheet.getRow(47);
			if(nmshdata.get(syear+"")!=null) {
				row47.getCell(5).setCellValue(objToDou(nmshdata.get(syear+"").get("wsclxzcs")));
			}
			if(nmshdata.get((syear+1)+"")!=null) {
				row47.getCell(6).setCellValue(objToDou(nmshdata.get((syear+1)+"").get("wsclxzcs")));
			}
			if(nmshdata.get(eyear+"")!=null) {
				row47.getCell(7).setCellValue(objToDou(nmshdata.get(eyear+"").get("wsclxzcs")));
			}
			//行政村总数
			HSSFRow row49 = sheet.getRow(49);
			if(nmshdata.get(syear+"")!=null) {
				row49.getCell(5).setCellValue(objToDou(nmshdata.get(syear+"").get("xzcs")));
			}
			if(nmshdata.get((syear+1)+"")!=null) {
				row49.getCell(6).setCellValue(objToDou(nmshdata.get((syear+1)+"").get("xzcs")));
			}
			if(nmshdata.get(eyear+"")!=null) {
				row49.getCell(7).setCellValue(objToDou(nmshdata.get(eyear+"").get("xzcs")));
			}
			//强制下载不打开
    		response.setContentType("application/octet-stream");
            //使用URLEncoder来防止文件名乱码或者读取错误
            response.setHeader("Content-Disposition", "attachment; filename="+URLEncoder.encode(syear+"-"+eyear+"考核指标表.xls", "UTF-8"));
            wb.write(out);
        } catch (Exception e) {
            logger.error("生成模板文件异常",e);
            try {
				out.write(new String("导出数据失败").getBytes("utf-8"));
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
	//种植业数据
	private Map<String, Map> getzzydata(int syear, int eyear) {
		LandResourceQueryVO query1 = new LandResourceQueryVO();
		query1.setSyear(syear);
		query1.setEyear(eyear);
		query1.setPage(1);
		query1.setLimit(Integer.MAX_VALUE);
		List<Map> zzydata = landResourceMapper.findAllForPage(query1);
		Map ret = new HashMap();
		if(null != zzydata) {
			for (Map map : zzydata) {
				ret.put(map.get("year").toString(), map);
			}
		}
		return ret;
	}
	private Map<String, GrassInfo> getcystdata(int syear, int eyear) {
		GrassQueryVO query2 = new GrassQueryVO();
		query2.setPage(1);
		query2.setLimit(Integer.MAX_VALUE);
		query2.setDate_year(syear+"");
		query2.setDate_year1(eyear+"");
		List<GrassInfo> cystdata = grassInfoMapper.queryInfoForPage(query2);
		Map<String, GrassInfo> ret = new HashMap();
		if(null != cystdata) {
			for (GrassInfo o : cystdata) {
				ret.put(o.getDate_year(), o);
			}
		}
		return ret;
		
	}
	//农产品信息数据
	private Map<String, FishInfo> getncpdata(int syear, int eyear) {
		FarmProductQueryVO query3 = new FarmProductQueryVO();
		query3.setPage(1);
		query3.setLimit(Integer.MAX_VALUE);
		query3.setDate_year(syear+"");
		query3.setDate_year1(eyear+"");
		List<FishInfo> ncpdata = productInfoMapper.queryInfoForPage(query3);
		Map<String, FishInfo> ret = new HashMap();
		if(null != ncpdata) {
			for (FishInfo o : ncpdata) {
				ret.put(o.getDate_year(), o);
			}
		}
		return ret;
	}
	//农业投入数据
	private Map<String, Map> getnytrdata(int syear, int eyear) {
		AgriInputinfoQueryVO query4 = new AgriInputinfoQueryVO();
		query4.setSyear(syear);
		query4.setEyear(eyear);
		query4.setPage(1);
		query4.setLimit(Integer.MAX_VALUE);
		List<Map> nytrdata = agriInputinfoMapper.findAllForPage(query4);
		Map<String, Map> ret = new HashMap();
		if(null != nytrdata) {
			for (Map o : nytrdata) {
				ret.put(o.get("year").toString(), o);
			}
		}
		return ret;
		
	}
	//农民生活数据
	private Map<String, Map> getnmshdata(int syear, int eyear) {
		NmshInfoQueryVO query5 = new NmshInfoQueryVO();
		query5.setSyear(syear);
		query5.setEyear(eyear);
		query5.setPage(1);
		query5.setLimit(Integer.MAX_VALUE);
		List<Map> nmshdata = nmshInfoMapper.findAllForPage(query5);
		Map<String, Map> ret = new HashMap();
		if(null != nmshdata) {
			for (Map o : nmshdata) {
				ret.put(o.get("year").toString(), o);
			}
		}
		return ret;
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
	
	private void setRowValForYearData(HSSFSheet sheet, List<Node> nodelist, String sp) {
		for (Node node : nodelist) {
			rownum_local.set(rownum_local.get()+1);
			HSSFRow row = sheet.getRow(rownum_local.get());
			row.getCell(0).setCellValue(sp+node.getData().get("target_name"));
			row.getCell(1).setCellValue(ObjToStr(node.getData().get("nccl")));
			row.getCell(2).setCellValue(ObjToStr(node.getData().get("nfmc")));
			row.getCell(3).setCellValue(ObjToStr(node.getData().get("child_size")));
			row.getCell(4).setCellValue(ObjToStr(node.getData().get("survival_size")));
			row.getCell(5).setCellValue(ObjToStr(node.getData().get("death_size")));
			row.getCell(6).setCellValue(ObjToStr(node.getData().get("maturity_size")));
			row.getCell(7).setCellValue(ObjToStr(node.getData().get("sell_size")));
			row.getCell(8).setCellValue(ObjToStr(node.getData().get("meat_output")));
			row.getCell(9).setCellValue(ObjToStr(node.getData().get("milk_output")));
			row.getCell(10).setCellValue(ObjToStr(node.getData().get("egg_output")));
			row.getCell(11).setCellValue(ObjToStr(node.getData().get("hair_output")));
			if(null != node.getChildren() && node.getChildren().size() > 0) {
				setRowValForYearData(sheet, node.getChildren(),sp+"   ");
			}
		}
	}
	private void setRowValForMonthData(HSSFSheet sheet, List<Node> nodelist, String sp) {
		for (Node node : nodelist) {
			rownum_local.set(rownum_local.get()+1);
			HSSFRow row = sheet.getRow(rownum_local.get());
			row.getCell(0).setCellValue(sp+node.getData().get("target_name"));
			row.getCell(1).setCellValue(ObjToStr(node.getData().get("nccl")));
			row.getCell(2).setCellValue(ObjToStr(node.getData().get("surplus_size")));
			row.getCell(3).setCellValue(ObjToStr(node.getData().get("female_size")));
			row.getCell(4).setCellValue(ObjToStr(node.getData().get("child_size")));
			row.getCell(5).setCellValue(ObjToStr(node.getData().get("czs_tq")));
			row.getCell(6).setCellValue(ObjToStr(node.getData().get("survival_size")));
			row.getCell(7).setCellValue(ObjToStr(node.getData().get("chs_tq")));
			row.getCell(8).setCellValue(ObjToStr(node.getData().get("chl")));
			row.getCell(9).setCellValue(ObjToStr(node.getData().get("chl_tq")));
			row.getCell(10).setCellValue(ObjToStr(node.getData().get("death_size")));
			row.getCell(11).setCellValue(ObjToStr(node.getData().get("sws_tq")));
			row.getCell(12).setCellValue(ObjToStr(node.getData().get("swl")));
			row.getCell(13).setCellValue(ObjToStr(node.getData().get("swl_tq")));
			row.getCell(14).setCellValue(ObjToStr(node.getData().get("maturity_size")));
			row.getCell(15).setCellValue(ObjToStr(node.getData().get("sell_size")));
			row.getCell(16).setCellValue(ObjToStr(node.getData().get("meat_output")));
			row.getCell(17).setCellValue(ObjToStr(node.getData().get("milk_output")));
			row.getCell(18).setCellValue(ObjToStr(node.getData().get("egg_output")));
			row.getCell(19).setCellValue(ObjToStr(node.getData().get("hair_output")));
			if(null != node.getChildren() && node.getChildren().size() > 0) {
				setRowValForMonthData(sheet, node.getChildren(),sp+"   ");
			}
		}
	}
	private String getTargetName(String target) {
		if("surplus_size".equals(target)) {
			return "月末存栏数";
		}else if("female_size".equals(target)) {
			return "能繁殖母畜";
		}else if("child_size".equals(target)) {
			return "产仔数";
		}else if("survival_size".equals(target)) {
			return "成活数";
		}else if("death_size".equals(target)) {
			return "损亡数";
		}else if("maturity_size".equals(target)) {
			return "出栏数";
		}else if("sell_size".equals(target)) {
			return "出售数";
		}else if("meat_output".equals(target)) {
			return "肉产量";
		}else if("milk_output".equals(target)) {
			return "奶产量";
		}else if("egg_output".equals(target)) {
			return "蛋产量";
		}else if("hair_output".equals(target)) {
			return "毛产量";
		}
		return "";
	}
	/**
	 * 畜牧业生产情况监控
	 * @param setData 监控设置数据
	 */
	public void dataMonitorHandle(List<Map> setData) {
		info("处理畜牧业生产情况监控开始");
		if(null != setData && setData.size() > 0) {
			AnimalsBreedQueryVO vo = new AnimalsBreedQueryVO();
			vo.setPage(1);
			vo.setLimit(Integer.MAX_VALUE);
			vo.setStopflag(1);
			vo.setDate_month(Integer.valueOf(new SimpleDateFormat("yyyyMM").format(new Date())));
			List<Map> list = animalsBreedMapper.findAllForPage(vo);
			if(null != list && list.size() > 0) {
				//统计父节点合计数
				for (Map map : list) {
					//如果是叶子节点，将本节点数据合计到父节点中
					if(((Integer) map.get("isleaf"))==1) {
						totalParent(list, map, (Integer) map.get("parent_id"));
					}
				}
				
				//判断规则
				for (Map map1 : list) {
					for (Map map2 : setData) {
						if((Integer) map1.get("fgid")==(Integer) map2.get("target_type")) {
							String target=(String) map2.get("target");
							String conditions = (String) map2.get("conditions");
							Double d1 = null != map1.get(target)?Double.valueOf(map1.get(target).toString()):null;
							Double d2 = null != map2.get("value_set")?Double.valueOf(map2.get("value_set").toString()):null;
							if(d1 != null && d2 != null) {
								String log=null;
								double ratio=0;
								if (">".equals(conditions) && d1>d2) {
									if(d2!=0) {
										ratio = new BigDecimal((d1-d2)+"").divide(new BigDecimal(d2+"")).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
									}
									log=vo.getDate_month()+"月"+map1.get("target_name")+"实际"+getTargetName(target)+d1+"，大于预警值"+d2;
								}else if ("<".equals(conditions) && d1<d2) {
									if(d2!=0) {
										ratio = new BigDecimal((d2-d1)+"").divide(new BigDecimal(d2+"")).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
									}
									log=vo.getDate_month()+"月"+map1.get("target_name")+"实际"+getTargetName(target)+d1+"，小于预警值"+d2;
								} else if ("=".equals(conditions) && d1==d2) {
									log=vo.getDate_month()+"月"+map1.get("target_name")+"实际"+getTargetName(target)+d1+"，等于预警值"+d2;
								}
								if(log != null) {
									info("畜牧业生产情况监控开预警信息保存");
									MonitorLog l = new MonitorLog();
									l.setStopflag(1);
									l.setSetgid((Integer) map2.get("gid"));
									l.setLog(log);
									l.setRatio(ratio);
									monitorLogMapper.insert(l);
								}
							}
						}
					}
				}
			}
		}
		info("处理畜牧业生产情况监控结束");
	}
	/**
	 * 按月统计产量
	 * @param map
	 * @return
	 */
	public Map getSumGroupYear(){
		info("按月统计产量");
		Map ret = new HashMap<>();
		
		Double[] meat_output = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
		Double[] milk_output = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
		Double[] egg_output = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
		Double[] hair_output = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
		
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		Map param = new HashMap<>();
		param.put("m1", Integer.valueOf(year+"01"));
		param.put("m2", Integer.valueOf(year+"12"));
		List<Map> list = animalsBreedMapper.getSumGroupYear(param);
		if(list == null || list.size() == 0) {
			info("当年无数据，查询上年数据");
			year=year-1;
			param.put("m1", Integer.valueOf((year-1)+"01"));
			param.put("m2", Integer.valueOf((year-1)+"12"));
			list = animalsBreedMapper.getSumGroupYear(param);
		}
		if(list == null || list.size() == 0) {
			info("上年无数据直接返回");
			return null;
		}
		Map<String, Map> monthData=new HashMap<>();
		//按月分组
		for (Map map : list) {
			monthData.put(map.get("date_month").toString(),map);
		}
		for(int i=1;i<=12;i++) {
			Map tmpmap = monthData.get(""+(year*100+i));
			if(null != tmpmap) {
				meat_output[i-1]=null==tmpmap.get("meat_output")?0:Double.valueOf(tmpmap.get("meat_output").toString());
				milk_output[i-1]=null==tmpmap.get("milk_output")?0:Double.valueOf(tmpmap.get("milk_output").toString());
				egg_output[i-1]=null==tmpmap.get("egg_output")?0:Double.valueOf(tmpmap.get("egg_output").toString());
				hair_output[i-1]=null==tmpmap.get("hair_output")?0:Double.valueOf(tmpmap.get("hair_output").toString());
			}
		}
		ret.put("meat_output", meat_output);
		ret.put("milk_output", milk_output);
		ret.put("egg_output", egg_output);
		ret.put("hair_output", hair_output);
		ret.put("year", year);
		return ret;
	}
	
	public Map monthDataAnalysis(Integer year,Integer target_type,String userid){
		info("按月统计分类数据：year="+year+",target_type="+target_type);
		Map ret = new HashMap<>();
		int cm = Integer.parseInt(new SimpleDateFormat("yyyyMM").format(new Date()));
		int sm = Integer.parseInt(year+"01");
		for(int i=sm;i<=cm;i++) {
			ret.put(i, getMonthData(i, userid));
		}
		return ret;
	}
	
	private void info(String msg) {
		if (logger.isInfoEnabled()) {
			logger.info(msg);
		}
	}
	private String ObjToStr(Object o) {
		if (null != o) {
			return o.toString();
		}
		return "";
	}
	private double objToDou(Object o) {
		if (null != o) {
			return Double.valueOf(o.toString());
		}
		return 0;
	}
}
