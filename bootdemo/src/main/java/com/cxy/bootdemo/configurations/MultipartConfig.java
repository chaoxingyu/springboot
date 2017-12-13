package com.cxy.bootdemo.configurations;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.web.servlet.MultipartConfigFactory;

//@Configuration
public class MultipartConfig {

	/**
	 * springboot 上传文件大小限制解决方法2 
	 * 解决方法1:在配置文件中设置参数
	 * @return
	 */
	// @Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		// 单个文件最大
		factory.setMaxFileSize("10240KB"); // KB,MB
		/// 设置总上传数据总大小
		factory.setMaxRequestSize("102400KB");
		return factory.createMultipartConfig();
	}
}
