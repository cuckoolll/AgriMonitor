package com.agri.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.AgriNewsInfo;
import com.agri.monitor.vo.AgriNewsQueryVO;

@Repository
public interface AgriNewsMapper {
	List<Map> queryInfoForPage(final AgriNewsQueryVO queryVo);
	
	int queryInfoCount(final AgriNewsQueryVO queryVo);
	
	void insertInfo(AgriNewsInfo agriNewsInfo);
	
	String queryGid(final String title);

	void delInfoByGid(List<Integer> gids);

	AgriNewsInfo findById(Integer gid);

	void updateInfo(AgriNewsInfo agriNewsInfo);
}
