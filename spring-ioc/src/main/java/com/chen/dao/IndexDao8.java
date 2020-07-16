package com.chen.dao;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Author liu
 * @Date 2020-07-12 16:15
 */
@Component
public class IndexDao8 implements FactoryBean {
	@Override
	public Object getObject() throws Exception {
		return new Student2();
	}

	@Override
	public Class<?> getObjectType() {
		return Student2.class;
	}
}
