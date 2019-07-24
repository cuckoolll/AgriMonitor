package com.agri.monitor.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agri.monitor.entity.RoleInfo;
import com.agri.monitor.entity.UserInfo;
import com.agri.monitor.enums.CacheTypeEnum;
import com.agri.monitor.enums.LogOptSatusEnum;
import com.agri.monitor.enums.LogOptTypeEnum;
import com.agri.monitor.mapper.UserinfoMapper;
import com.agri.monitor.utils.CacheUtil;
import com.agri.monitor.utils.LogUtil;
import com.agri.monitor.vo.UserQueryVO;

@Service
public class UserInfoService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserInfoService.class);
	
	@Autowired
	private UserinfoMapper userinfoMapper;
	
	public UserInfo findById(String gid, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("获取用户信息，GID=" + gid);
		}
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询用户信息，GID="+gid);
		return userinfoMapper.findById(gid);
	}
	
	public Map doDel(List<Integer> gids, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("用户数据删除开始：" + gids);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			userinfoMapper.delete(gids);
			UserQueryVO queryVO3 = new UserQueryVO();
			queryVO3.setPage(1);
			queryVO3.setLimit(Integer.MAX_VALUE);
			CacheUtil.putCache(CacheTypeEnum.USER, userinfoMapper.findAllForPage(queryVO3));
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.SUCESS, userid, "用户数据删除，GID="+gids);
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("删除用户信息异常" + e);
			LogUtil.log(LogOptTypeEnum.DEL, LogOptSatusEnum.FAIL, userid, "用户数据删除异常："+e.getMessage());
		}
		return result;
	}
	
	public Map saveOrUpdate(UserInfo userinfo,int type,String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("用户数据更新开始：" + userinfo);
		}
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 0);
		try {
			userinfo.setModifier(userid);
			//更新
			if(type==2) {
				userinfo.setLast_time(new Date());
				userinfoMapper.update(userinfo);
				LogUtil.log(LogOptTypeEnum.UPDATE, LogOptSatusEnum.SUCESS, userid, "用户数据更新"+userinfo);
			}else {//新增
				UserInfo u = userinfoMapper.findById(userinfo.getUser_id());
				if(null != u) {
					result.put("code", -1);
					result.put("msg", "用户登录名"+userinfo.getUser_id()+"已存在");
					logger.error("用户登录名"+userinfo.getUser_id()+"已存在");
					return result;
				}
				
				userinfo.setCreator(userid);
				userinfoMapper.insert(userinfo);
				LogUtil.log(LogOptTypeEnum.ADD, LogOptSatusEnum.SUCESS, userid, "新增用户数据"+userinfo);
			}
			UserQueryVO queryVO3 = new UserQueryVO();
			queryVO3.setPage(1);
			queryVO3.setLimit(Integer.MAX_VALUE);
			CacheUtil.putCache(CacheTypeEnum.USER, userinfoMapper.findAllForPage(queryVO3));
		} catch (Exception e) {
			result.put("code", -1);
			logger.error("保存用户信息异常" + e);
			LogUtil.log(LogOptTypeEnum.SAVE, LogOptSatusEnum.FAIL, userid, "保存用户数据异常："+e.getMessage());
		}
		return result;
	}
	
	public List<Map> findAllForPage(UserQueryVO queryVO, String userid) {
		if (logger.isInfoEnabled()) {
			logger.info("查询所有用户数据开始：" + queryVO);
		}
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userid, "查询用户信息，"+queryVO);
		return userinfoMapper.findAllForPage(queryVO);
	}
	
	public int findAllCount(UserQueryVO queryVO) {
		return userinfoMapper.findAllCount(queryVO);
	}
	
	public UserInfo findForLogin(String user_id, String user_password, HttpServletRequest request) {
		Map m = new HashMap<>();
		m.put("user_id",user_id);
		m.put("user_password",user_password);
		UserInfo u = userinfoMapper.findForLogin(m);
		if (null != u) {
			u.setRoleinfo(userinfoMapper.findRole(u.getUser_role()));
			request.getSession().setAttribute("userinfo", u);
		}
		return u;
	}
	
	public RoleInfo findRole(Integer gid) {
		if (logger.isInfoEnabled()) {
			logger.info("查询用户角色" + gid);
		}
		return userinfoMapper.findRole(gid);
	}
	
	public boolean savepw(UserInfo userinfo, HttpServletRequest request) {
		if (logger.isInfoEnabled()) {
			logger.info("用户修改密码：" + userinfo.getUser_id());
		}
		userinfoMapper.updpw(userinfo);
		LogUtil.log(LogOptTypeEnum.QUERY, LogOptSatusEnum.SUCESS, userinfo.getUser_id(), "用户修改密码：" + userinfo.getUser_id());
		request.getSession().removeAttribute("userinfo");
		return true;
	}
}
