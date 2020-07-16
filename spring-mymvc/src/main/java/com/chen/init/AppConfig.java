package com.chen.init;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * @Author liu
 * @Date 2019-10-22 20:24
 */
//WebMvcConfigurationSupport 里面有很多的@Bean,会帮我们往spring容器中注入很多的bean,如HanderlMapping
@Configuration
@ComponentScan("com.chen")
public class AppConfig extends WebMvcConfigurationSupport {

	/**
	 * 其实在servlet3.0规范中已经支持了下载文件，
	 * 且springboot已经支持了这个，在springboot开发中根本不需要配置这个，springboot已经配置好了
	 * 然后为什么必须非得是multipartResolver这个名字呢，这是因为spring希望我们在项目中只有一个支持
	 * 下载文件的，(@RequestPart("zilu") MultipartFile multipart)，如果springboot配置了，
	 * 我们想要覆盖只需要在下面配置这个bean,因为名字相同就会去覆盖对应的配置
	 *
	 */
//	@Bean("multipartResolver") //必须叫这个名字
//	public CommonsMultipartResolver commonsMultipartResolver(){
//		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
//		return commonsMultipartResolver;
//	}

	/**
	 * 配置视图解析器
	 * @return
	 */
	@Bean
	public InternalResourceViewResolver internalResourceViewResolver() {
		InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
		internalResourceViewResolver.setPrefix("/");
		internalResourceViewResolver.setSuffix(".jsp");
		return internalResourceViewResolver;
	}



}
