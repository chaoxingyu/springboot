package com.cxy.baseboot2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Baseboot2Application {

	private static Logger LOG = LoggerFactory.getLogger(Baseboot2Application.class);

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(Baseboot2Application.class, args);
		String[] activeProfiles = ctx.getEnvironment().getActiveProfiles();
		if (null == activeProfiles || 1 > activeProfiles.length) {
			LOG.info("Spring Boot 2 未使用 配置文件");
			return;
		}
		for (String profile : activeProfiles) {
			LOG.info("Spring Boot 2 使用 配置文件 为:" + profile);
		}
	}
}
