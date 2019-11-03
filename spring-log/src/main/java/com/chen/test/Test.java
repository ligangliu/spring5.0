package com.chen.test;

import com.chen.app.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**

 * @Author liu
 * @Date 2019-11-02 10:34
 */
public class Test {
	public static void main(String[] args) {
		/**
		 * spring5 默认是jul打印日志,spring5
		 * spring4 默认是log4j打印日志,sping4当中依赖了jcl
		 */
		AnnotationConfigApplicationContext context =
				new AnnotationConfigApplicationContext(AppConfig.class);
		context.start();
	}
}
