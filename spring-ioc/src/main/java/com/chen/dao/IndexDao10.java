package com.chen.dao;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @Author liu
 * @Date 2020-07-26 11:42
 */
public class IndexDao10 implements ImportSelector {

	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		return new String[]{"com.chen.dao.Student3", "com.chen.dao.Student4"};
	}
}
