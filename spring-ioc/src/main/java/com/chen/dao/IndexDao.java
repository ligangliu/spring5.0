package com.chen.dao;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Author liu
 * @Date 2019-10-18 16:23
 */
//@Component
public class IndexDao {

	public IndexDao() {
		System.out.println("index dao");
	}

	public void query() {
		System.out.println("query");
	}

	@Bean
	public Person getPerson() {
		return new Person();
	}

}
