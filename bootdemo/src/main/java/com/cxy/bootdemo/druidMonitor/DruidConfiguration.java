package com.cxy.bootdemo.druidMonitor;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

/**
 * Druid的DataResource配置类 凡是被Spring管理的类，实现接口 EnvironmentAware 重写方法 setEnvironment
 * 可以在工程启动时， 获取到系统环境变量和application配置文件中的变量。
 * 还有一种方式是采用注解的方式获取 @value("${变量的key值}") 获取application配置文件中的变量。 这里采用第一种要方便些
 * 
 */
@Configuration
public class DruidConfiguration implements EnvironmentAware {

	private static final Logger logger = LoggerFactory.getLogger(DruidConfiguration.class);

	// 系统环境变量和application配置文件中的变量
	private RelaxedPropertyResolver propertyResolver;

	@Override
	public void setEnvironment(Environment environment) {
		this.propertyResolver = new RelaxedPropertyResolver(environment, "spring.datasource.");
	}
	
	// 启动springboot时初始化连接池
	@Bean(name = "dataSource", destroyMethod = "close", initMethod = "init") 
	@Primary // 在同样的DataSource中，首先使用被标注的DataSource
	public DataSource dataSource() {
		DruidDataSource datasource = new DruidDataSource();
		datasource.setDriverClassName(propertyResolver.getProperty("driverClassName"));
		datasource.setUrl(propertyResolver.getProperty("url"));
		datasource.setUsername(propertyResolver.getProperty("username"));
		datasource.setPassword(propertyResolver.getProperty("password"));
		// 其他配置项
		datasource.setInitialSize(Integer.valueOf(propertyResolver.getProperty("initialSize")));
		datasource.setMinIdle(Integer.valueOf(propertyResolver.getProperty("minIdle")));
		datasource.setMaxActive(Integer.valueOf(propertyResolver.getProperty("maxActive")));
		datasource.setMaxWait(Long.valueOf(propertyResolver.getProperty("maxWait")));
		datasource.setTimeBetweenEvictionRunsMillis(Long.valueOf(propertyResolver.getProperty("timeBetweenEvictionRunsMillis")));
		datasource.setMinEvictableIdleTimeMillis(Long.valueOf(propertyResolver.getProperty("minEvictableIdleTimeMillis")));
		datasource.setValidationQuery(propertyResolver.getProperty("validationQuery"));
		datasource.setTestWhileIdle(Boolean.valueOf(propertyResolver.getProperty("testWhileIdle")));
		datasource.setTestOnBorrow(Boolean.valueOf(propertyResolver.getProperty("testOnBorrow")));
		datasource.setTestOnReturn(Boolean.valueOf(propertyResolver.getProperty("testOnReturn")));
		boolean poolPreparedStatements = Boolean.valueOf(propertyResolver.getProperty("poolPreparedStatements"));
		datasource.setPoolPreparedStatements(poolPreparedStatements);
		if (poolPreparedStatements) {
			datasource.setMaxPoolPreparedStatementPerConnectionSize(
							Integer.valueOf(propertyResolver.getProperty("maxPoolPreparedStatementPerConnectionSize")));
		}
		try {
			// Druid内置提供了四种LogFilter（Log4jFilter、Log4j2Filter、CommonsLogFilter、Slf4jLogFilter）
			// 他们的别名分别是log4j、log4j2、slf4j、commonlogging和commonLogging。其中commonlogging和commonLogging只是大小写不同。
			// https://github.com/alibaba/druid/wiki/%E5%86%85%E7%BD%AEFilter%E7%9A%84%E5%88%AB%E5%90%8D
			datasource.setFilters(propertyResolver.getProperty("filters"));
		} catch (SQLException e) {
			logger.error("阿里数据库连接池Druid 初始化 filter 错误", e);
			e.printStackTrace();
		}
		datasource.setConnectionProperties(propertyResolver.getProperty("connectionProperties"));
		datasource.setUseGlobalDataSourceStat(Boolean.valueOf(propertyResolver.getProperty("useGlobalDataSourceStat")));
		//  Druid锁的公平模式问题
		// 	缺省unfair，通过构造函数传入参数指定fair或者unfair；如果DruidDataSource还没有初始化，修改maxWait大于0，自动转换为fair模式
		//  unfair: 并发性能很好  	fair:公平，但是并发性能很差
		//  datasource.setUseUnfairLock(true);
		logger.info("阿里数据库连接池Druid 初始化完毕  !");
		return datasource;
	}

	@Bean
	public ServletRegistrationBean druidServlet() {
		logger.info("init Druid Servlet Configuration ");
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
		// 添加初始化参数：initParams
		// IP白名单(没有配置或者为空，则允许所有访问)
		// servletRegistrationBean.addInitParameter("allow", "192.168.2.25,127.0.0.1");
		// IP黑名单(共同存在时，deny优先于allow)
		// servletRegistrationBean.addInitParameter("deny", "192.168.1.100");
		// 控制台管理用户
		servletRegistrationBean.addInitParameter("loginUsername", "dbname");
		servletRegistrationBean.addInitParameter("loginPassword", "dbpwd");
		// 是否能够重置数据 禁用HTML页面上的“Reset All”功能
		servletRegistrationBean.addInitParameter("resetEnable", "false");
		return servletRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
		// 添加过滤规则.
		filterRegistrationBean.addUrlPatterns("/*");
		// 添加不需要忽略的格式信息.
		filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		return filterRegistrationBean;
	}

}
