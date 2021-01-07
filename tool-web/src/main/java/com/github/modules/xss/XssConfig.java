package com.github.modules.xss;

import com.google.common.collect.Maps;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
 

import java.util.Map;
 
@Configuration
public class XssConfig{
 
	/**
	 * xss过滤拦截器
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	@Bean
	public FilterRegistrationBean xssFilterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new XssFilter());
		filterRegistrationBean.setOrder(1);
		filterRegistrationBean.setEnabled(true);
		filterRegistrationBean.addUrlPatterns("/*");
		Map<String, String> initParameters = Maps.newHashMap();
		//excludes用于配置不需要参数过滤的请求url
		initParameters.put("excludes", "/favicon.ico,/images/*,/js/*,/css/*");
		//isIncludeRichText默认为true，主要用于设置富文
		initParameters.put("isIncludeRichText", "true");
		filterRegistrationBean.setInitParameters(initParameters);
		return filterRegistrationBean;
	}
}
