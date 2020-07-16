package com.chen.dao;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author liu
 * @Date 2019-10-18 16:23
 */
@Component("indexDao1")
public class IndexDao1 implements ApplicationContextAware {

	@Autowired
	private IndexDao2 indexDao2;

	public void query() {
		System.out.println("query");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

	}
}
