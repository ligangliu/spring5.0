package com.chen.test;

import com.chen.app.AppConfig;
import com.chen.dao.IndexDao13;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author liu
 * @Date 2019-10-18 16:27
 */
public class Test {

	public static void main(String[] args) throws Exception {
		//把spring所有的前提环境准备好
		AnnotationConfigApplicationContext context =
				new AnnotationConfigApplicationContext(AppConfig.class);

		IndexDao13 indexDao13 = context.getBean(IndexDao13.class);
		indexDao13.test();
	}

}
