package com.chen.init;

import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

/**
 * @Author liu
 * @Date 2020-07-13 10:41
 */

/**
 1) Spring MVC 0xml 配置流程
 servlet3.0规范的支持
 2) Spring MVC 源码分析
 3) 从发请求到Controller中间经过的流程

request --> Controller   Controller就是一个普通的java类
 理论上request 在tomcat中只能对应servlet
 所以request -> Controller，所以reqeust一定是先到servlet的。然后：
 1.方法调用,所以底层一定是反射，需要知道类型+方法名
 2.转发，一个servlet->另一个servlet

 DispatcherServlet().setLoadOnStartup(1);tomcat就会去立即执行init方法。我们看
 springmvc源码就需要从种类跟进去

 在initHandlerMappings()会找到spring中所有的实现了HandlerMapping的接口的类，
 其中有一个非常重要的(找到@Controller的信息) RequestMappingHandlerMapping
 是非常重要的，我们可以跟进他的源码看一看

 总结起来
 	DispatcherServlet中的static {
 	ClassPathResource resource = new ClassPathResource(DEFAULT_STRATEGIES_PATH, DispatcherServlet.class);
 		defaultStrategies = PropertiesLoaderUtils.loadProperties(resource);
 	}
 	加载spring-webmvc中配置文件中8个配置类，放到defaultStrategies中
 	tomcat启动-执行DispatcherServlet的init()方法
 	init() -> initServletBean() -> initWebApplicationContext() -> onRefresh() ->
 	initStrategies() 这个里面做一些列的初始化工作

 */
public class TomcatApplication {
	public static void main(String[] args) {
		try {
			System.out.println("gggggg");
			Tomcat tomcat = new Tomcat();
			tomcat.setPort(9999);
			/**
			 * 如果tomcat没有加addContext的话，就会在当前工作空间默认生成一个tomcat.port的目录
			 * 但是这样的话，tomcat就不会认为这是一个web项目，那么就会按照servlet3.0的规范去执行
			 * 实现了WebApplicationInitializer接口的类(那么就无法初始化spring容器呀，
			 * 无法获取到ServletContext servletContext,就无法去添加DispatcherServlet，
			 * 当然也可以按照SpringApplicationChen中的那样)
			 * 所以还是需要去执行下面添加tomcat.addWebapp来，告诉tomcat这是一个web项目，需要
			 * 遵守servlet3.0的规范
			 * addWebapp 同时也是告诉tomcat项目源码所在位置
			 *
			 */
//			File base = new File(System.getProperty("java.io.tmpdir"));
//			Context rootContext = tomcat.addContext("/", base.getAbsolutePath());
			/**
			 * 但是在这里启动的时候，并不会去执行MyWebApplicationInitializer implements WebApplicationInitializer
			 * 实现了WebApplicationInitializer接口，上面提到servlet3.0规范是需要去执行的，
			 * 那么为什么这里不执行呢？
			 * 这是因为tomcat并不认为这是一个web项目，所以不会去遵守servlet3.0规范
			 *
			 * 但是tomcat.addWebapp()之后就会去执行实现了WebApplicationInitializer接口的类，
			 *
			 * (启动的时候需要等待一小段时间)但是这里会报一个java.lang.ClassNotFoundException: org.apache.jasper.servlet.JspServlet的错
			 * 这是因为tomcat.addWebapp，tomat认为这是一个web项目，tomcat会做一个jsp执行引擎，但是我们当前
			 * 的项目并没有jsp的依赖，但是我们的项目并不依赖jsp,所以不用管（当然也可以在这里加上一个依赖）
			 *
			 *
			 * */
			// 如果没有jsp等的控制，就不需要去管这些，直接tomcat.addWebapp即可
			Context ctx = tomcat.addWebapp("/", "e:\\"); // 并且会提示not find web.xml
			WebResourceRoot resources = new StandardRoot(ctx);
			String sourcePath = TomcatApplication.class.getResource("/").getPath();
			resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
					sourcePath, "/*"));

			tomcat.start();
			System.out.println("hhhhhhhhhhhhhhhhhhhhh");
			tomcat.getServer().await();
		} catch (Exception e) {

		}
	}
}
