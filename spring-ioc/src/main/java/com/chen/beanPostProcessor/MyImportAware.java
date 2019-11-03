package com.chen.beanPostProcessor;

import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @Author liu
 * @Date 2019-10-20 12:57
 */
public class MyImportAware implements ImportAware {

	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
	}
}
