package com.ibero.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MvcConfig implements WebMvcConfigurer {
	
	@Autowired
	private HandlerInterceptor intercepWeb;


	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		WebMvcConfigurer.super.addResourceHandlers(registry);
		registry.addResourceHandler("/Web_Fotos/**").addResourceLocations("file:/C://Web_Fotos/");
	}


	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		WebMvcConfigurer.super.addInterceptors(registry);
		registry.addInterceptor(intercepWeb)
		.addPathPatterns("/**")
		.excludePathPatterns("/login", "/cerrado","/css/**", "/js/**", "/images/**","/Web_Fotos/**");
	}
	
	

	

}
