package com.cxy.bootdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
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

}
