package com.chen.dao;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * @Author liu
 * @Date 2020-07-26 11:50
 */

@Component
public class IndexDao11 implements FactoryBean {

	@Override
	public Object getObject() throws Exception {
		return new Student4();
	}

	@Override
	public Class<?> getObjectType() {
		return Student4.class;
	}
}
