package com.chen.dao;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @Author liu
 * @Date 2020-08-16 9:37
 */
@Component
public class IndexDao13 {

	@Autowired
	private ApplicationContext context;


	@Autowired
	private BeanFactory beanFactory;


	public void test() {
		System.out.println(context);
		System.out.println(beanFactory);
	}

}
