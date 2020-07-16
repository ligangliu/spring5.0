package com.chen.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @Author liu
 * @Date 2019-10-20 17:01
 */
@Service
public class IndexDao3 {

//	private IndexDao4 indexDao4;
//
//	public IndexDao3(IndexDao4 indexDao4) {
//		this.indexDao4 = indexDao4;
//	}

	@Bean
	public IndexDao4 indexDao4() {
		return new IndexDao4();
	}


}
