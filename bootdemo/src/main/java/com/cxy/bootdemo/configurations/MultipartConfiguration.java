package com.cxy.bootdemo.configurations;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.web.servlet.MultipartConfigFactory;

/**
 * 
 * @package com.cxy.bootdemo.configurations
 * @type MultipartConfiguration
 * @description 上传文件配置
 * @author cxy
 * @date 2017年12月13日 下午5:50:37
 * @version 1.0.0
 */
// @Configuration
public class MultipartConfiguration {

	/**
	 * springboot 上传文件大小限制解决方法2 
	 * 解决方法1:在配置文件中设置参数
	 * 
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
