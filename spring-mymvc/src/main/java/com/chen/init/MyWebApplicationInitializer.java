package com.chen.init;

import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;


/**
 * @Author liu
 * @Date 2019-10-22 20:25
 */

/**
 * 这个方法是Servlet3.0的规范，
 * 由tomcat自己主动调用
 * 但是这样写，会有问题，为什么呢?
 */
public class MyWebApplicationInitializer implements WebApplicationInitializer {
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {

	}
}
