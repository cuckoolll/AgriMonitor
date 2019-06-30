package com.agri.monitor.config;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.agri.monitor.interceptor.LoginInterceptor;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

/*	@Bean
    public FilterRegistrationBean filterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new LoginFilter());
        registration.addUrlPatterns("/*");
       // registration.addInitParameter("paramName", “”);
        registration.setName("LoginFilter");
        registration.setOrder(1);
        return registration;
    }*/
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器
        InterceptorRegistration loginRegistry = registry.addInterceptor(new LoginInterceptor());
        // 拦截路径
        loginRegistry.addPathPatterns("/**");
        // 排除路径
        loginRegistry.excludePathPatterns("/");
        loginRegistry.excludePathPatterns("/doLogin");
        loginRegistry.excludePathPatterns("/loginout");
        loginRegistry.excludePathPatterns("/error");
        // 排除资源请求
        loginRegistry.excludePathPatterns("/login.html");
        loginRegistry.excludePathPatterns("/css/**");
        loginRegistry.excludePathPatterns("/images/**");
        loginRegistry.excludePathPatterns("/js/**");
        loginRegistry.excludePathPatterns("/temp/**");
    }
	
    @Bean  
    public MultipartConfigElement multipartConfigElement() {  
        MultipartConfigFactory factory = new MultipartConfigFactory();  
        //单个文件最大  
        factory.setMaxFileSize(DataSize.parse("5MB")); //KB,MB  
        //设置总上传数据总大小  
        factory.setMaxRequestSize(DataSize.parse("50MB"));  
        return factory.createMultipartConfig();  
    }  
}