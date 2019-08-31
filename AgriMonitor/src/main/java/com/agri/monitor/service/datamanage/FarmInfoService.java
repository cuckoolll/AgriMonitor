package com.agri.monitor.service.datamanage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

import com.agri.monitor.entity.FarmInfo;
import com.agri.monitor.entity.MonitorLog;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.CacheTypeEnum;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.FarmInfoMapper;
import com.agri.monitor.mapper.MonitorLogMapper;
import com.agri.monitor.utils.CacheUtil;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.vo.FarmQueryVO;

@Service
public class FarmInfoService {
	
	private static final Logger logger = LoggerFactory.getLogger(FarmInfoService.class);
	
	@Autowired
	private FarmInfoMapper farmInfoMapper;
	@Autowired
	private MonitorLogMapper monitorLogMapper;
	
	public FarmInfo findById(Integer gid, String userid) {
		info("获取养殖信息，GID=" + gid);
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询养殖场信息，GID="+gid);
		return farmInfoMapper.findById(gid);
	}
	
	public Map doDel(List<Integer> gids, String userid) {
		info("养殖场数据删除开始：" + gids);
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			farmInfoMapper.delete(gids);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "养殖场数据删除，GID="+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除养殖场信息异常" + e);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "养殖场数据删除异常："+e.getMessage());
		}
		return result;
	}
	
	public Map saveOrUpdate(FarmInfo farminfo,String userid) {
		info("养殖场数据更新开始：" + farminfo);
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			farminfo.setModifier(userid);
			//更新
			if(!StringUtils.isEmpty(farminfo.getGid())) {
				farminfo.setLast_time(new Date());
				farmInfoMapper.update(farminfo);
				LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "养殖场数据更新"+farminfo);
			}else {//新增
				farminfo.setCreator(userid);
				farmInfoMapper.insert(farminfo);
				LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, userid, "新增养殖场数据"+farminfo);
			}
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("保存养殖场信息异常" + e);
			LogUtil.log(LogOptTypeEnum.SAVE, LogOptSatusEnum.FAIL, userid, "保存养殖场数据异常："+e.getMessage());
		}
		return result;
	}
	
	public List<Map> findAllForPage(FarmQueryVO farmQueryVO, String userid) {
		info("查询所有养殖场数据开始：" + farmQueryVO);
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询养殖场信息，"+farmQueryVO);
		return farmInfoMapper.findAllForPage(farmQueryVO);
	}
	
	public int findAllCount(FarmQueryVO farmQueryVO) {
		return farmInfoMapper.findAllCount(farmQueryVO);
	}
	
	public Map dataImport(MultipartFile file, HttpServletRequest request) {
		info("养殖场数据导入开始");
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
        	info("养殖场数据导入，文件名：" + filename);
	        if (prefix.equals("xlsx")) {
	        	wb = new XSSFWorkbook(file.getInputStream());
	        } else if (prefix.equals("xls")) {
	        	wb = new HSSFWorkbook(file.getInputStream());
	        } else {
        		info("养殖场数据导入，不支持的文件类型");
	        	result.put("code", -1);
	    		result.put("msg", "不支持的文件类型");
	    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(),"不支持的文件类型");
	    		return result;
	        }
	        
	        Sheet sheet1 = wb.getSheetAt(0);
	        int i = 0;
	        List<FarmInfo> list = new ArrayList<>();
	        for (Row row : sheet1) {
	        	//解析所属乡镇
				/*
				 * if (i == 1) { towns = row.getCell(1).getStringCellValue();
				 * if(StringUtils.isEmpty(towns)) { result.put("code", -1); result.put("msg",
				 * "乡镇未填写"); return result; } }
				 */
	           if (i >= 2) {
	        	   FarmInfo farminfo = new FarmInfo();
	        	   String towns = row.getCell(0).getStringCellValue();
	        	   if(StringUtils.isEmpty(towns)) {
	        		   result.put("code", -1); 
	        		   result.put("msg", "第"+(i+1)+"行乡镇未填写"); 
	        		   return result; 
	        	   }
	        	   farminfo.setTowns(towns);
	        	   farminfo.setFarm_name(row.getCell(1).getStringCellValue());
	        	   farminfo.setFarm_address(row.getCell(2).getStringCellValue());
	        	   farminfo.setLegal_person(row.getCell(3).getStringCellValue());
	        	   farminfo.setPhone_num(row.getCell(4).getStringCellValue());
	        	   String name=row.getCell(5).getStringCellValue();
	        	   if(StringUtils.isEmpty(name)) {
	        		   result.put("code", -1);
	        		   result.put("msg", "第"+(i+1)+"行认定畜种未填写");
	        		   return result;
	        	   }
	        	   name=name.trim();
	        	   Integer type = getAnimalsType(name);
	        	   if(null == type) {
	        		   result.put("code", -1);
	        		   result.put("msg", "第"+(i+1)+"行认定畜种在系统中未维护");
	        		   return result;
	        	   }
	        	   farminfo.setAnimals_type(type);
	        	   farminfo.setAnimals_size(Integer.valueOf(row.getCell(6).getStringCellValue()));
	        	   if (row.getCell(7) != null) {
	        		   farminfo.setRemarks(row.getCell(6).getStringCellValue());
	        	   }
	        	   farminfo.setCreator(user.getUser_id());
	        	   farminfo.setModifier(user.getUser_id());
	        	   farminfo.setCounty("刚察县");
	        	   list.add(farminfo);
	           }
	           i++;
	        }
	        farmInfoMapper.batchInsert(list);
	        LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.SUCESS, user.getUser_id(), "导入养殖场信息，共导入"+list.size()+"条");
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("养殖场数据导入，解析文件异常", e);
			}
			result.put("code", -1);
    		result.put("msg", "解析文件失败");
    		LogUtil.log(LogOptTypeEnum.IMPORT, LogOptSatusEnum.FAIL, user.getUser_id(), "导入养殖场信息异常："+e.getMessage());
		}
		return result;
	}
	
	private Integer getAnimalsType(String name) {
		if (StringUtils.isEmpty(name)) {
			return null;
		}
		List<Map> list = (List<Map>) CacheUtil.getCache(CacheTypeEnum.ANIMALSTYPE);
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
	/**
	 * 农场数据监控
	 * @param setData1 养殖场牲畜总存栏监控设置数据
	 * @param setData2 单个养殖场牲畜存栏监控设置数据
	 */
	public void dataMonitorHandle(List<Map> setData1,List<Map> setData2) {
		info("处理养殖场数据监控开始");
		//养殖场牲畜总存栏监控
		if (null != setData1 && setData1.size() > 0) {
			info("养殖场牲畜总存栏监控");
			//统计所有分类
			List<Integer> animaltype = new ArrayList<>();
			for (Map map : setData1) {
				animaltype.add((Integer) map.get("target_type"));
			}
			//按牲畜类型统计合计值
			Map<String, Double> sumMap = new HashMap<>();
			List<Map> sumList = farmInfoMapper.findSumByType(animaltype);
			//判断规则
			if(null != sumList && sumList.size() > 0) {
				for (Map map1 : sumList) {
					for (Map map2 : setData1) {
						if((Integer) map1.get("animals_type")==(Integer) map2.get("target_type")) {
							String conditions = (String) map2.get("conditions");
							Double d1 = null != map1.get("animals_size")?Double.valueOf(map1.get("animals_size").toString()):null;
							Double d2 = null != map2.get("value_set")?Double.valueOf(map2.get("value_set").toString()):null;
							if(d1 != null && d2 != null) {
								String log=null;
								double ratio=0;
								if (">".equals(conditions) && d1>d2) {
									if(d2!=0) {
										ratio = new BigDecimal((d1-d2)+"").divide(new BigDecimal(d2+"")).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
									}
									log=map1.get("type_name")+"实际总存栏数"+d1+"，大于预警值"+d2;
								}else if ("<".equals(conditions) && d1<d2) {
									log=map1.get("type_name")+"实际总存栏数"+d1+"，小于预警值"+d2;
									if(d2!=0) {
										ratio = new BigDecimal((d2-d1)+"").divide(new BigDecimal(d2+"")).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
									}
								} else if ("=".equals(conditions) && d1==d2) {
									log=map1.get("type_name")+"实际总存栏数"+d1+"，等于预警值"+d2;
								}
								if(log != null) {
									info("养殖场牲畜总存栏监控预警信息保存");
									MonitorLog l = new MonitorLog();
									l.setStopflag(1);
									l.setSetgid((Integer) map2.get("gid"));
									l.setLog(log);
									l.setRatio(ratio);
									monitorLogMapper.insert(l);
								}
							}
							break;
						}
					}
				}
			}
		}
		//单个养殖场牲畜存栏监控
		if(null != setData2 && setData2.size() > 0) {
			info("单个养殖场牲畜存栏监控");
			//统计所有分类
			List<Integer> types = new ArrayList<>();
			for (Map map : setData2) {
				types.add((Integer) map.get("target_type"));
			}
			
			List<Map> list = farmInfoMapper.findNumByType(types);
			if(null != list && list.size() > 0) {
				//判断规则
				for (Map map1 : list) {
					for (Map map2 : setData2) {
						if((Integer) map1.get("animals_type")==(Integer) map2.get("target_type")) {
							String conditions = (String) map2.get("conditions");
							Double d1 = null != map1.get("animals_size")?Double.valueOf(map1.get("animals_size").toString()):null;
							Double d2 = null != map2.get("value_set")?Double.valueOf(map2.get("value_set").toString()):null;
							if(d1 != null && d2 != null) {
								String log=null;
								double ratio=0;
								if (">".equals(conditions) && d1>d2) {
									if(d2!=0) {
										ratio = new BigDecimal((d1-d2)+"").divide(new BigDecimal(d2+"")).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
									}
									log=map1.get("farm_name")+""+map1.get("type_name")+"实际存栏数"+d1+"，大于预警值"+d2;
								}else if ("<".equals(conditions) && d1<d2) {
									if(d2!=0) {
										ratio = new BigDecimal((d2-d1)+"").divide(new BigDecimal(d2+"")).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
									}
									log=map1.get("farm_name")+""+map1.get("type_name")+"实际存栏数"+d1+"，小于预警值"+d2;
								} else if ("=".equals(conditions) && d1==d2) {
									log=map1.get("farm_name")+""+map1.get("type_name")+"实际存栏数"+d1+"，等于预警值"+d2;
								}
								if(log != null) {
									info("单个养殖场牲畜存栏监控预警信息保存");
									MonitorLog l = new MonitorLog();
									l.setStopflag(1);
									l.setSetgid((Integer) map2.get("gid"));
									l.setRatio(ratio);
									l.setLog(log);
									monitorLogMapper.insert(l);
								}
							}
							break;
						}
					}
				}
			}
		}
		info("处理养殖场数据监控结束");
	}
	
	//首页养殖场统计图
	public Map findSumGroupTowns(){
		info("首页养殖场统计图开始");
		Map ret = new HashMap<>();
		List<Map> list = farmInfoMapper.findSumGroupTowns();
		
		if(null != list && list.size() > 0) {
			//地图数据
			Map<String, List<Map>> temmap = new HashMap<>();
			for (Map map : list) {
				List<Map> l = temmap.get(map.get("towns").toString());
				if (null == l) {
					l = new ArrayList<>();
					l.add(map);
					temmap.put(map.get("towns").toString(), l);
				}else {
					l.add(map);
				}
			}
			List<Map> temlist = new ArrayList<>();
			for (Entry<String, List<Map>> map : temmap.entrySet()) {
				Map m = new HashMap<>();
				m.put("name", map.getKey());
				m.put("size", 100);
				m.put("data", map.getValue());
				temlist.add(m);
			}
			ret.put("mapdata", temlist);
			
			Map<String, Double> temmap1 = new HashMap<>();
			//牲畜占比
			for (Map map : list) {
				Double d1 = temmap1.get(map.get("type_name").toString());
				if(null == d1) {
					temmap1.put(map.get("type_name").toString(),Double.valueOf(map.get("animals_size").toString()));
				}else {
					temmap1.put(map.get("type_name").toString(),Double.valueOf(map.get("animals_size").toString())+d1);
				}
			}
			List<Map> temlist1 = new ArrayList<>();
			for (Entry<String, Double> map : temmap1.entrySet()) {
				Map m = new HashMap<>();
				m.put("name", map.getKey());
				m.put("value", map.getValue());
				temlist1.add(m);
			}
			ret.put("piedata", temlist1);
		}
		
		info("首页养殖场统计图结束");
		return ret;
	}
	
	private void info(String msg) {
		if (logger.isInfoEnabled()) {
			logger.info(msg);
		}
	}
}
