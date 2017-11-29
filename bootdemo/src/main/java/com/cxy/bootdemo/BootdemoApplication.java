package com.cxy.bootdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
// @Configuration
public class BootdemoApplication {

	private static Logger logger = LoggerFactory.getLogger(BootdemoApplication.class);

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(BootdemoApplication.class, args);
		String[] activeProfiles = ctx.getEnvironment().getActiveProfiles();
		if (null == activeProfiles || activeProfiles.length == 0) {
			logger.error("Spring Boot 未使用 配置文件");
			return;
		}
		for (String profile : activeProfiles) {
			logger.info("Spring Boot 使用 配置文件 为:" + profile);
		}
	}

	/**
	 * springboot 上传文件大小限制解决方法2
	 * 	解决方法1:在配置文件中设置参数
	 * @return

	// @Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		// 单个文件最大
		factory.setMaxFileSize("10240KB"); // KB,MB
		/// 设置总上传数据总大小
		factory.setMaxRequestSize("102400KB");
		return factory.createMultipartConfig();
	}
	 */

}
