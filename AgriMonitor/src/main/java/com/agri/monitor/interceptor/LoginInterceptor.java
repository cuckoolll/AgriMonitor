package com.agri.monitor.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.agri.monitor.annotation.IgnoreSession;
import com.agri.monitor.entity.UserInfo;

public class LoginInterceptor implements HandlerInterceptor {
	
	/**
     * 在请求被处理之前调用
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	// 如果不是映射到方法直接通过
//        if (!(handler instanceof HandlerMethod)) {
//            return true;
//        }
//    	HandlerMethod handlerMethod = (HandlerMethod) handler;
//    	Method method = handlerMethod.getMethod();
//    	//忽略session是否有效
//    	if (method.isAnnotationPresent(IgnoreSession.class)) {
//            return true;
//        }
//        // 检查每个到来的请求对应的session域中是否有登录标识
//        Object userinfo = request.getSession().getAttribute("userinfo");
//        if (null == userinfo || !(userinfo instanceof UserInfo)) {
//            // 未登录，重定向到登录页
//            response.sendRedirect(request.getContextPath()+"/login.html");
//            return false;
//        }
        return true;
    }

    /**
     * 在请求被处理后，视图渲染之前调用
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 在整个请求结束后调用
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
