package com.chen.test;

import com.chen.app.AppConfig;
import com.chen.beanPostProcessor.MyBeanFactoryPostProcessor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

/**
 * @Author liu
 * @Date 2019-10-18 16:27
 */
public class Test {

	public static void main(String[] args) {
		//把spring所有的前提环境准备好
		AnnotationConfigApplicationContext context =
				new AnnotationConfigApplicationContext();
		context.register(AppConfig.class);
		context.addBeanFactoryPostProcessor(new MyBeanFactoryPostProcessor());
		context.refresh();
		String[] names = context.getBeanDefinitionNames();
		Arrays.stream(names).forEach(System.out::println);
	}

}
