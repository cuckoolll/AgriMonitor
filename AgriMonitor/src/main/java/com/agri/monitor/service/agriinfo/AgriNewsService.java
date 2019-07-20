package com.agri.monitor.service.agriinfo;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		result.put("data", agriNewsMapper.queryInfoForPage(queryVo));
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
	
	
}
