package com.agri.monitor.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.agri.monitor.entity.AgriNewsInfo;
import com.agri.monitor.vo.AgriNewsQueryVO;

@Repository
public interface AgriNewsMapper {
	List<AgriNewsInfo> queryInfoForPage(final AgriNewsQueryVO queryVo);
	
	int queryInfoCount(final AgriNewsQueryVO queryVo);
	
	void insertInfo(AgriNewsInfo agriNewsInfo);
	
	String queryGid(final String title);

	void delInfoByGid(List<Integer> gids);
}
