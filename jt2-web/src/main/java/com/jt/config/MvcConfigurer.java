package com.jt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.jt.interceptor.UserInterceptor;

//原先整合SpringMVC要配置web.xml，要配置DispatcherServlet，要配置spring-config.xml，配置視圖解析器
//現在使用SpringBoot整合好了，這些配置信息都放在WebMvcConfigurer類裡，不用配了，要是沒有重寫裡面的方法就是默認設置，
//但是默認是沒辦法訪問.html的頁面的，如果想訪問.html，就要修改他原有的配置
//WebMvcConfigurer是WebMVC原有的配置，然後我們重寫了一個方法，讓他會攔截有.xxx後綴的請求
//如下配置後http://www.jt.com/items/562379.html或是http://www.jt.com/items/562379.sadhjk都可以
@Configuration
public class MvcConfigurer implements WebMvcConfigurer{
	
	@Autowired
	private UserInterceptor userInterceptor;
	//开启匹配后缀型配置
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		
		configurer.setUseSuffixPatternMatch(true);
	}
	
	/**新增攔截器配置
	 * url:http://www.jt.com/cart/xxxxx
	 *     http://www.jt.com/order/xxxxx
	 * 攔截路徑:
	 * /*  只能攔下一級路徑
	 * /** 攔截所有的路徑
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//添加我們自定義的攔截器
		registry.addInterceptor(userInterceptor)
		        .addPathPatterns("/cart/**", "/order/**");
	}
}
