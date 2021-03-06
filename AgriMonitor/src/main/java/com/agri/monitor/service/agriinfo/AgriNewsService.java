package com.agri.monitor.service.agriinfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.agri.monitor.entity.AgriNewsInfo;
import com.agri.monitor.entity.SoilInfo;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.AgriNewsMapper;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.vo.AgriNewsQueryVO;

@Service
@Transactional
public class AgriNewsService {
	
	private static final Logger logger = LoggerFactory.getLogger(AgriNewsService.class);
	
	private SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private AgriNewsMapper agriNewsMapper;
	 
	public Map queryInfoForPage(AgriNewsQueryVO queryVo, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("获取农业信息，入参=title:" + queryVo.getTitle());
		}

		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		result.put("msg", "成功");
		result.put("count", agriNewsMapper.queryInfoCount(queryVo));
		List<Map> list = agriNewsMapper.queryInfoForPage(queryVo);
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
				"获取农业信息，入参=title:" + queryVo.getTitle());
		return result;
	}
	
	public Map delInfoByGid(List<Integer> gids, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("农业信息删除开始：" + gids);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			agriNewsMapper.delInfoByGid(gids);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "农业信息删除，GID="+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除农业信息异常" + e);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "农业信息删除异常："+e.getMessage());
		}
		return result;
	}
	
	public AgriNewsInfo findById(Integer gid, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("获取农业信息，GID=" + gid); 
		}
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询农业信息，GID="+gid); 
		return agriNewsMapper.findById(gid); 
	}
	
	public Map saveOrUpdate(AgriNewsInfo agriNewsInfo, String userid) { 
		if (logger.isInfoEnabled()) { 
			logger.info("农业信息数据更新开始：" + agriNewsInfo); 
		} 
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0); 
		
		try { 
			agriNewsInfo.setModifier(String.valueOf(userid));
			//更新 
			if(!StringUtils.isEmpty(agriNewsInfo.getGid())) {
				agriNewsMapper.updateInfo(agriNewsInfo); 
				LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "农业信息数据更新" + agriNewsInfo); 
			} else {
				//新增
				agriNewsInfo.setCreator(String.valueOf(userid));
				agriNewsInfo.setAuthor(userid);
				agriNewsMapper.insertInfo(agriNewsInfo); 
				LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, userid, "农业信息测数据" + agriNewsInfo); 
			} 
		} catch (Exception e) {
			result.put("code", -1); logger.error("保存土壤监测数据异常" + e);
			LogUtil.log(LogOptTypeEnum.SAVE, LogOptSatusEnum.FAIL, userid, "保存土壤监测数据异常：" + e.getMessage()); 
		}
		return result;
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
