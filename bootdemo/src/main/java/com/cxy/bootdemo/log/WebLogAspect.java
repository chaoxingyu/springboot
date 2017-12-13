package com.cxy.bootdemo.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;

@Component // 首先初始化切面类
@Aspect // 声明为切面类，底层使用动态代理实现AOP
public class WebLogAspect {

	private Logger logger = LoggerFactory.getLogger(WebLogAspect.class);

	/*
	 * 1、AOP用在哪些方面：AOP能够将那些与业务无关，却为业务模块所共同调用的逻辑或责任，例如事务处理、日志管理、权限控制，异常处理等，封装起来，
	 * 便于减少系统的重复代码，降低模块间的耦合度，并有利于未来的可操作性和可维护性。 2、AOP中的概念：
	 * Aspect(切面):指横切性关注点的抽象即为切面,它与类相似,只是两者的关注点不一样,类是对物体特征的抽象,而切面是横切性关注点的抽象.
	 * 
	 * joinpoint(连接点):所谓连接点是指那些被拦截到的点（可以是方法、属性、或者类的初始化时机(可以是Action层、Service层、dao层)）。
	 * 在spring中,这些点指的是方法,因为spring只支持方法类型的连接点,实际上joinpoint还可以是field或类构造器)
	 * 
	 * Pointcut(切入点):所谓切入点是指我们要对那些joinpoint进行拦截的定义，也即joinpoint的集合.
	 * 
	 * Advice(通知):所谓通知是指拦截到joinpoint之后所要做的事情就是通知.通知分为前置通知,后置通知,异常通知,最终通知,环绕通知
	 * 
	 * Target(目标对象):代理的目标对象
	 * 
	 * Weave(织入):指将aspects应用到target对象并导致proxy对象创建的过程称为织入.
	 * 
	 * Introduction(引入):在不修改类代码的前提下, Introduction可以在运行期为类动态地添加一些方法或Field.
	 * 
	 * 3、AOP带来的好处：：降低模块的耦合度；使系统容易扩展；更好的代码复用性
	 * 
	 * 
	 * 类注解:
	 * 
	 * @Aspect将一个类定义为一个切面类
	 * 
	 * @order(i)标记切面类的处理优先级,i值越小,优先级别越高.PS:可以注解类,也能注解到方法上 方法注解:
	 * 
	 * @Pointcut定义一个方法为切点里面的内容为一个表达式,下面详细介绍
	 * 
	 * @Before 在切点前执行方法,内容为指定的切点
	 * 
	 * @After 在切点后,return前执行,
	 * 
	 * @AfterReturning在切入点,return后执行,如果想对某些方法的返回参数进行处理,可以在这操作
	 * 
	 * @Around 环绕切点,在进入切点前,跟切点后执行
	 * 
	 * @AfterThrowing 在切点后抛出异常进行处理
	 * 
	 * @order(i) 标记切点的优先级,i越小,优先级越高
	 * 
	 */

	// 如果要设置多个切点可以使用 || 拼接
	// 设置通讯处理切面
    /**
     * Pointcut 是指那些方法需要被执行"AOP",是由"Pointcut Expression"来描述的.
	   Pointcut可以有下列方式来定义或者通过&& || 和!的方式进行组合. 
			args()
			@args()
			execution()
			this()
			target()
			@target()
			within()
			@within()
			@annotation
		其中execution 是用的最多的,其格式为:
	execution(modifier-pattern? ret-type-pattern declaring-type-pattern?  name-pattern(param-pattern) throws-pattern?) 
	pattern分别表示修饰符匹配（modifier-pattern?）、返回值匹配（ret-type-pattern）、类路径匹配（declaring-type-pattern?）、方法名匹配（name-pattern）、
	参数匹配（(param-pattern)）、异常类型匹配（throws-pattern?），其中后面跟着“?”的是可选项。
	returning type pattern,name pattern, and parameters pattern是必须的.
	ret-type-pattern:可以为*表示任何返回值,全路径的类名等.
	name-pattern:指定方法名,*代表所以,set*,代表以set开头的所有方法.
	parameters pattern:指定方法参数(声明的类型),(..)代表所有参数,(*)代表一个参数,(*,String)代表第一个参数为任何值,第二个为String类型.
	*/
	@Pointcut("execution(public * com.cxy.bootdemo.controller..*.*(..))")
	public void webLog() {

	}

	@Around(value = "webLog()")
	public Object aroundss(ProceedingJoinPoint pjp) throws Throwable {
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");
		String startTime = ldt.format(dtf);
		// 接收到请求，并记录请求内容
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		String url = request.getRequestURL().toString();
		String scheme = request.getScheme();
		logger.info("访问  " + url + " 开始时间 :{}", startTime);
		Map<String, String> map = new LinkedHashMap<>();
		map.put("URL", url);
		map.put("HttpMethod", request.getMethod());
		map.put("RemoteAddr", request.getRemoteAddr());
		map.put("ClassMethod", pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName());
		map.put("args", Arrays.toString(pjp.getArgs()));
		map.put("AuthType", request.getAuthType());
		map.put("CharacterEncoding", request.getCharacterEncoding());
		map.put("ContentType", request.getContentType());
		map.put("ContextPath", request.getContextPath());
		map.put("LocalAddr", request.getLocalAddr());
		map.put("LocalName", request.getLocalName());
		map.put("RemoteUser", request.getRemoteUser());
		map.put("RemoteHost", request.getRemoteHost());
		map.put("RequestedSessionId", request.getRequestedSessionId());
		map.put("Scheme", scheme);

		String str = JSONObject.toJSONString(map);
		logger.info(scheme + " 上行请求数据: {}", str);
		Object[] arguments = pjp.getArgs();
		
		// 校验上送参数
		// || !listOfValuesLookupUtil.isValidApplication(arguments[0].toString())
		if (arguments.length == 0) {
			HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
			response.sendError(HttpStatus.PRECONDITION_FAILED.value(), "请求数据错误！");
			logEnd(url, scheme, ldt, "请求数据错误！");
			return null;
		}
		Object obj = pjp.proceed();
		logEnd(url, scheme, ldt, obj);
		return obj;
	}

	/**
	 * 记录结束日志
	 * 
	 * @param url
	 *            请求url
	 * @param scheme
	 *            请求类型
	 * @param ldt
	 *            开始时间
	 * @param obj
	 *            返回数据
	 */
	private void logEnd(String url, String scheme, LocalDateTime ldt, Object obj) {
		LocalDateTime endTime = LocalDateTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");
		logger.info("访问  " + url + " 结束时间: " + endTime.format(dtf) + " 共耗时: {}", ChronoUnit.MILLIS.between(ldt, endTime) + "毫秒");
		logger.info(scheme + " 下行返回数据: {}", obj);
	}

	// 前置通知
	// 在切点方法集合执行前，执行前置通知
	// @Before("webLog()")
	public void doBefore(JoinPoint joinPoint) throws Throwable {
		// 接收到请求，记录请求内容
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		// 记录下请求内容
		logger.info("URL : {}", request.getRequestURL().toString());
		logger.info("HTTP_METHOD : {}", request.getMethod());
		logger.info("IP : {}", request.getRemoteAddr());
		logger.info("CLASS_METHOD : {}", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
		logger.info("ARGS : {}", Arrays.toString(joinPoint.getArgs()));
	}

	// @After(value = "webLog()")
	public void doAfterBeforeReturning(JoinPoint joinPoint) {
		logger.info("@After ----RESPONSE : " + Arrays.toString(joinPoint.getArgs()));
	}

	// 后置通知
	// @AfterReturning(returning = "obj", pointcut = "webLog()")
	public void doAfterReturning(Object obj) throws Throwable {
		// 处理完请求，返回内容
		logger.info("RESPONSE : {}", obj);
	}

}
