package com.chen.app;

import com.chen.dao.IndexDao;
import com.chen.dao.IndexDao5;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Repository;

/**
 * @Author liu
 * @Date 2019-10-18 16:22
 */
@Configuration
@ComponentScan("com.chen")
//@EnableAspectJAutoProxy
public class AppConfig {

	@Bean
	public IndexDao indexDao() {
		return new IndexDao();
	}

	@Bean
	public IndexDao5 indexDao5() {
		return new IndexDao5();
	}


	/*@Bean
	public *//*static*//* IndexDao1 indexDao1() {
		indexDao();
		return new IndexDao1();
	}*/



}
//class A implements ImportSelector {
//	@Override
//	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
//		return new String[0];
//	}
//}
