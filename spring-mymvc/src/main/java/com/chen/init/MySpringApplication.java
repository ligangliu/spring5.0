package com.chen.init;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;

/**
 * @Author liu
 * @Date 2019-10-22 20:28
 *
 * request --> Controller   Controller就是一个普通的java类
 * 理论上request 在tomcat中只能对应servlet
 * 所以request -> Controller
 * 1.方法调用,所以底层一定是反射
 * 2.转发，一个servlet->另一个servlet
 *
 * 在initHandlerMappings()会找到spring中所有的实现了HandlerMapping的接口的类，
 * 其中有一个非常重要的(找到@Controller的信息) RequestMappingHandlerMapping
 * 是非常重要的，我们可以跟进他的源码看一看
 *
 */
public class MySpringApplication {
	public static void run() throws LifecycleException {
		//初始化我们的spring容器
		AnnotationConfigWebApplicationContext context =
				new AnnotationConfigWebApplicationContext();
		context.register(AppConfig.class);
//		context.refresh();

		Tomcat tomcat = new Tomcat();
		tomcat.setPort(9090);

		File base = new File(System.getProperty("java.io.tmpdir"));
		/**
		 * addWebapp tomcat就会认为这是一个web项目
		 * 项目就应该是web目录
		 * 所以这里不能用addwebapp
		 *
		 * 如果是web项目，tomcat就会去加载一些额外的东西，如jsp
		 * 如 tomcat 就需要依赖 tomcat-embed-jasper
		 *
		 * 所以springboot是不支持jsp，加了一个jsp依赖的话，但是
		 * springboot并不能去解释jsp,它就不是一个web项目，所以需要addContext
		 * 表示这不是一个web项目
		 *
		 * 但是addContext就不能去初始化spring容器
		 */
//		tomcat.addWebapp("/",base.getAbsolutePath());
		Context rootContext = tomcat.addContext("/", base.getAbsolutePath());
		DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
		//tomcat中添加一个servlet
		/**
		 * .setLoadOnStartup(1) 表示tomcat启动过程，就会调用Dispatcher的init方法
		 * mvc request ---- servlet.class 但是Controller并不是servlet.class
		 * 那么request如何到达Controller?转发(转发至jsp或其他servlet)，
		 * 方法调用?很明显时方法调用(底层肯定是用反射，类名 + 方法名)。
		 *
		 * 所以setloadOnstartup(1)执行Dispatcher中的init方法。
		 * 而在init方法中就会初始化我们的Controller和请求的映射
		 *
		 * 总结起来
		 * DispatcherServlet中的static {
		 *     ClassPathResource resource = new ClassPathResource(DEFAULT_STRATEGIES_PATH, DispatcherServlet.class);
		 * 		defaultStrategies = PropertiesLoaderUtils.loadProperties(resource);
		 * }
		 * 加载spring-webmvc中配置文件中8个配置类，放到defaultStrategies中
		 * tomcat启动-执行DispatcherServlet的init()方法
		 * init() -> initServletBean() -> initWebApplicationContext() -> onRefresh() ->
		 * initStrategies() 这个里面做一些列的初始化工作
		 *
		 *
		 */
		tomcat.addServlet(rootContext,"chen",dispatcherServlet).setLoadOnStartup(1);
		rootContext.addServletMapping("/*","chen");
		tomcat.start();
		tomcat.getServer().await();

	}

	public static void main(String[] args) throws LifecycleException {
		new MySpringApplication().run();
	}
}
