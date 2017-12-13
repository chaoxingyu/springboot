package com.cxy.bootdemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloWorldController {
	
/*
 *
@RequestMapping的属性(通用的注解，支持所有的HTTP请求)
  @RequestMapping此注解即可以作用在控制器的某个方法上，也可以作用在此控制器类上。
        当控制器在类级别上添加@RequestMapping注解时，这个注解会应用到控制器的所有处理器方法上。
        处理器方法上的@RequestMapping注解会对类级别上的@RequestMapping的声明进行补充。
  
  属性：
	value和path
		value和path是都是指请求地址。@RequestMapping("/login")等价于@RequestMapping(path="/login")。
	占位符(都支持带占位符的URL)
		参数名跟占位符名字一致的情况
		@RequestMapping(path = "/{account}", method = RequestMethod.GET)
		public String getUser(@PathVariable String account)
		参数名跟占位符名字不一致的情况
		@RequestMapping(path = "/{account}/{name}", method = RequestMethod.GET)
		public String getUser(@PathVariable("account") String phoneNumber,@PathVariable("name") String userName)
	@PathVariable
		@Pathvariable注解可以绑定占位符传过来的值到方法的参数上
	method 
		method属性是指请求的方式，一般使用get/post.
组合注解(RequestMapping的变形)(组合注解是方法级别的)
	@GetMapping 	= 	@RequestMapping(method = RequestMethod.GET)
	@PostMapping 	= 	@RequestMapping(method = RequestMethod.POST)
	@PutMapping		= 	@RequestMapping(method = RequestMethod.PUT)
	@DeleteMapping 	= 	@RequestMapping(method = RequestMethod.DELETE)
 *
@RequestParam
	A） 常用来处理简单类型的绑定，通过Request.getParameter() 获取的String可直接转换为简单类型的情况（ String--> 简单类型的转换操作由ConversionService配置的转换器来完成）；
因为使用request.getParameter()方式获取参数，所以可以处理get 方式中queryString的值，也可以处理post方式中 body data的值；
	B）用来处理Content-Type: 为 application/x-www-form-urlencoded编码的内容，提交方式GET、POST；
	C) 该注解有两个属性： value、required； value用来指定要传入值的id名称，required用来指示参数是否必须绑定；
	@RequestParam注解可以用来提取名为“xxx”的String类型的参数，并将之作为输入参数传入，这就是SpringMVC的提取和解析请求参数的能力。 	
	我们甚至可以不用这个注解，也能只要传入参数名和方法的参数名一致，也能匹配。
	如果传入参数名字和方法参数名字不一致，可以给@RequestParam的属性赋值。
 *
@RequestBody
	@RequestBody可以用来解析json字符串(还可以解析xml)，并将字符串映射到对应的实体中，实体的字段名和json中的键名要对应。
	注意提交请求的时候要在请求头指定content-type为application/json charset=utf-8。
	常用来处理Content-Type: 不是application/x-www-form-urlencoded编码的内容，例如application/json, application/xml等；
	它是通过使用HandlerAdapter 配置的HttpMessageConverters来解析post data body，然后绑定到相应的bean上的。
	因为配置有FormHttpMessageConverter，所以也可以用来处理 application/x-www-form-urlencoded的内容，
	处理完的结果放在一个MultiValueMap<String, String>里，这种情况在某些特殊需求下使用，详情查看FormHttpMessageConverter api;	
 *
@RequestHeader
	把Request请求header部分的值绑定到方法的参数上。
@CookieValue 
	可以把Request header中关于cookie的值绑定到方法的参数上。
 *
@SessionAttributes:
	该注解用来绑定HttpSession中的attribute对象的值，便于在方法中的参数里使用。
	该注解有value、types两个属性，可以通过名字和类型指定要使用的attribute 对象；
	
@ModelAttribute
	该注解有两个用法，一个是用于方法上，一个是用于参数上；
	用于方法上时：  通常用来在处理@RequestMapping之前，为请求绑定需要从后台查询的model；
	用于参数上时： 用来通过名称对应，把相应名称的值绑定到注解的参数bean上；要绑定的值来源于：
	A） @SessionAttributes 启用的attribute 对象上；
	B） @ModelAttribute 用于方法上时指定的model对象；
	C） 上述两种情况都没有时，new一个需要绑定的bean对象，然后把request中按名称对应的方式把值绑定到bean中。
 *
@RequestBody

作用： 
      i) 该注解用于读取Request请求的body部分数据，使用系统默认配置的HttpMessageConverter进行解析，然后把相应的数据绑定到要返回的对象上；
      ii) 再把HttpMessageConverter返回的对象数据绑定到 controller中方法的参数上。
使用时机：
A) GET、POST方式提时， 根据request header Content-Type的值来判断:
    application/x-www-form-urlencoded， 可选（即非必须，因为这种情况的数据@RequestParam, @ModelAttribute也可以处理，当然@RequestBody也能处理）；
    multipart/form-data, 不能处理（即使用@RequestBody不能处理这种格式的数据）；
    其他格式， 必须（其他格式包括application/json, application/xml等。这些格式的数据，必须使用@RequestBody来处理）；
B) PUT方式提交时， 根据request header Content-Type的值来判断:
    application/x-www-form-urlencoded， 必须；
    multipart/form-data, 不能处理；
    其他格式， 必须；
说明：request的body部分的数据编码格式由header部分的Content-Type指定；
@ResponseBody

作用： 
      该注解用于将Controller的方法返回的对象，通过适当的HttpMessageConverter转换为指定格式后，写入到Response对象的body数据区。
使用时机：
      返回的数据不是html标签的页面，而是其他某种格式的数据时（如json、xml等）使用；
 *
 */
	
	
	
    @RequestMapping(value="/say", method=RequestMethod.POST)
    public String sysHello() {
    		return "Say Hello World";
    }


    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @ResponseBody
    public String hello(@RequestParam String name) {
        return "Hello " + name;
    }
    

}

