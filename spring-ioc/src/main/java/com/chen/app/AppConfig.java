package com.chen.app;

import com.chen.dao.IndexDao;
import com.chen.dao.IndexDao10;
import com.chen.dao.IndexDao5;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Repository;

/**
 * @Author liu
 * @Date 2019-10-18 16:22
 */
@Configuration
@ComponentScan("com.chen")
@PropertySource("classpath:db.properties")
@Import(IndexDao10.class)
@EnableAspectJAutoProxy
public class AppConfig {

	@Value("${name}")
	private String name;

	@Value("${age}")
	private int age;

	@Bean
	public IndexDao indexDao() {
		System.out.println("indexDao......");
		return new IndexDao();
	}

	@Bean
	public IndexDao5 indexDao5() {
		System.out.println("indexDao5......");
		test();
		return new IndexDao5();
	}

	public void test() {
		System.out.println(name);
		System.out.println(age);
		System.out.println("test....");
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
