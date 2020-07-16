package com.chen.init;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;

/**
 * @Author liu
 * @Date 2020-07-13 16:22
 */

/**
 * 这样的话，就不用去实现 implements WebApplicationInitializer，并且不会报找不到jsp等逻辑
 * 那以后，就不需要再去构建web项目啦
 */
public class SpringApplicationChen {

	public static void main(String[] args) throws LifecycleException {
		// 启动tomcat
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.register(AppConfig.class);

		File base = new File(System.getProperty("java.io.tmpdir"));

		Tomcat tomcat = new Tomcat();
		tomcat.setPort(8888);

		Context rootCtx = tomcat.addContext("/", base.getAbsolutePath());
		DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
		// 直接添加Servlet，就不需要去按TomcatApplication中的那样
		Tomcat.addServlet(rootCtx, "chen", dispatcherServlet).setLoadOnStartup(1);
		rootCtx.addServletMapping("/", "chen");
		tomcat.start();
		tomcat.getServer().await();

	}

}
