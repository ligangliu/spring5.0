package com.chen.beanPostProcessor;

import com.chen.dao.IndexDao12;
import com.chen.dao.IndexDao9;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.stereotype.Component;

/**
 * @Author liu
 * @Date 2020-07-27 10:50
 */
@Component
public class ChenBeanFactoryBeanPostProcessor implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		GenericBeanDefinition indexDao12 =
				(GenericBeanDefinition)beanFactory.getBeanDefinition("indexDao12");
		indexDao12.getPropertyValues().addPropertyValue(new PropertyValue("name1", new IndexDao9()));
		indexDao12.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
	}
}
