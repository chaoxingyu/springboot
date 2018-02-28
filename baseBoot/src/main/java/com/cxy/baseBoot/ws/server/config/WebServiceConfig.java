package com.cxy.baseBoot.ws.server.config;

import com.cxy.baseBoot.ws.server.exception.DetailSoapFaultDefinitionExceptionResolver;
import com.cxy.baseBoot.ws.server.exception.ServiceFaultException;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import java.util.Properties;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

	/*
	@Override
	public void addInterceptors(List<EndpointInterceptor> interceptors) {
		PayloadValidatingInterceptor validatingInterceptor = new PayloadValidatingInterceptor();
		validatingInterceptor.setValidateRequest(true);
		validatingInterceptor.setValidateResponse(true);
		//validatingInterceptor.setx
		validatingInterceptor.setXsdSchema(countriesSchema());
		interceptors.add(validatingInterceptor);
	}
	*/

	@Bean
	public SoapFaultMappingExceptionResolver exceptionResolver(){
		SoapFaultMappingExceptionResolver exceptionResolver = new DetailSoapFaultDefinitionExceptionResolver();

		SoapFaultDefinition faultDefinition = new SoapFaultDefinition();
		faultDefinition.setFaultCode(SoapFaultDefinition.SERVER);
		exceptionResolver.setDefaultFault(faultDefinition);

		Properties errorMappings = new Properties();
		errorMappings.setProperty(Exception.class.getName(), SoapFaultDefinition.SERVER.toString());
		errorMappings.setProperty(ServiceFaultException.class.getName(), SoapFaultDefinition.SERVER.toString());

		exceptionResolver.setExceptionMappings(errorMappings);
		exceptionResolver.setOrder(1);
		return exceptionResolver;
	}

	@Bean
	public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		// PS:transformWsdlLocations设置成true，就是说当你改变了项目名称或者端口号，你的服务还是可以正常访问的（但是如果你改变了WSDL的访问路径，发布服务的代码也要随之更改的）
		return new ServletRegistrationBean(servlet, "/ws/*");
	}


	@Bean(name = "countries")
	public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema countriesSchema) {
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("CountriesPort");
		//wsdl11Definition.setLocationUri("/ws");
		//wsdl11Definition.setTargetNamespace("http://spring.io/guides/gs-producing-web-service");
		wsdl11Definition.setSchema(countriesSchema);
		// DefaultWsdl11Definition id 默认就是发布的ws的访问路径。
		// spring-ws发布的web service是以后缀.wsdl访问的，跟传统的?wsdl不大一样
		// spring-ws实际上把发布wsdl和真正的服务实现Endpoint分开了，
		// 若Endpoint不正确，很可能会出现浏览器访问.wsdl地址正常而客户端调用却出现Not Found 404的错误。
		// 原因：客户端要根据wsdl生成相应的代码，如果放在一起这些代码都有了就演示不到这一步了。
		return wsdl11Definition;
	}

	@Bean
	public XsdSchema countriesSchema() {
		return new SimpleXsdSchema(new ClassPathResource("/wsSchema/countries.xsd"));
	}

	@Bean(name = "employee")
	public DefaultWsdl11Definition defaultEmployeeWsdl11Definition(XsdSchema employeeSchema) {
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("EmployeePort");
		wsdl11Definition.setSchema(employeeSchema);
		return wsdl11Definition;
	}

	@Bean
	public XsdSchema employeeSchema() {
		return new SimpleXsdSchema(new ClassPathResource("/wsSchema/hr.xsd"));
	}


}
