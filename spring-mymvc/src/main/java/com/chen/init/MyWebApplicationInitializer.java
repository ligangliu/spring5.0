package com.chen.init;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.logging.Logger;


/**
 * @Author liu
 * @Date 2019-10-22 20:25
 */

/**

 传统的springmvc开发
 1） 初始化spring的环境  在web.xml中注册一个org.springframework.web.ContextLoaderListener,这个里面会去初始化spring环境
 2）读取一个xml(Tomcat的入口会去解析这个web.xml，可以理解为这是servlet2.5的规范)
 3）注册一个servlet（在容器启动的时候执行init方法）
 	扫描controller
    json解析器
    视图解析器

 spring开发
 	解决上面的1）2）3）
 	对于1）
	 AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
	 context.register(AppConfig.class);
 	对于2）：主要就是注册一个DispatchServlet,
 	使用的方案就是MyWebApplicationInitializer implements WebApplicationInitializer
 	3）然后在spring初始化中可以扫描@Controller
    4）内嵌了一个容器 tomcat.run呗，直接运行tomcat的main方法

 tomcat exe
 	我们传统启动tomcat -> 运行tomcat.ext -> 会去执行tomcat中的main()方法
    那么为啥我们程序员不去直接运行这个main方法呢？其实完全是可以的，springboot不就是这样的么


 下面的这个类，springmvc官方给的解决方案，用来替代web.xml
 但是有没有想过，tomcat启动的时候，为什么就会主动调用这个方法
 总不可能tomcat中start之后
 for(当前项目中所有实现了WebApplicationInitializer的对象) {
    调用对象的onStart()方法
 }
 但是tomcat和spring是不同的公司，总不可能人家tomcat去依赖spring代码。。。。

 所以这里就需要理解servlet3.0规范
 这是因为tomcat或jetty容器会遵守servlet3.0规范，然后它们会去classpath中找
 META-INFO/services/javax.servlet.ServletContainerInitializer(没有的话也不会报错)
 在里面配置一些类，如果里面的类实现了ServletContainerInitializer接口，
 (其实这里我们可以遵守这个规定，在里面配置一些我们重写的类，springboot就是遵守了这个规定)
 那么容器启动之后会去调用里面的onStartup方法。

 @HandlesTypes(WebApplicationInitializer.class)
 public class SpringServletContainerInitializer implements ServletContainerInitializer {
	 public void onStartup(@Nullable Set<Class<?>> webAppInitializerClasses, ServletContext servletContext)
	 throws ServletException {
	 }
 }

 但是有没有思考，上面的规范和我们下面的有什么关系呢？？？?
 按照这个规范，也调用不到我们下面的onStartup()方法，
 这是因为servlet3.0规范，
 在该类上有个注解，@HandlesTypes(WebApplicationInitializer.class)，然后容器会将classpath
 下的所有实现了该接口的类传到上面的方法的第一个参数Set<Class<?>> webAppInitializerClasses
 里面。这里是由tomcat自己实现的，（可能是通过扫描所有的classpath，然后通过反射得到的吧，
 具体如何实现等有空去看tomcat的源码。。。。


 */
public class MyWebApplicationInitializer implements WebApplicationInitializer {
	/**
	 * ServletContext: web组件，filter servlet, listener
	 */
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		// 初始化spring的环境和springweb环境
 		System.out.println("你会执行嘛。。。。。");

		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.register(AppConfig.class);
//		context.refresh();

		// 这里吧context传入进去，是在AppConfig中需要配置了一些替代web.xml的信息
		DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
		// 注册一个servlet容器
		ServletRegistration.Dynamic registration =
				servletContext.addServlet("dispatcherServlet", dispatcherServlet);
		registration.addMapping("/*");
		// 让tomcat启动就去调用DispatcherServlet的init方法
		/**
		 * .setLoadOnStartup(1) 表示tomcat启动过程，就会调用Dispatcher的init方法
		 *  mvc request ---- servlet.class 但是Controller并不是servlet.class
		 * 	那么request如何到达Controller?转发(转发至jsp或其他servlet)，
		 * 方法调用?很明显时方法调用(底层肯定是用反射，类名 + 方法名)。
		 * 所以setloadOnstartup(1)是在tomcat或jetty启动的时候，执行Dispatcher中的init方法。
		 * 具体是其子类中的org.springframework.web.servlet.HttpServletBean#init()
		 * 而在init方法中就会初始化我们的Controller和请求的映射
		 *
		 */
		registration.setLoadOnStartup(1);

	}
}
