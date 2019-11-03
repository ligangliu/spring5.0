package com.chen.app;

import com.chen.dao.IndexDao;
import com.chen.dao.IndexDao1;
import com.chen.dao.IndexDao2;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotationMetadata;

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
