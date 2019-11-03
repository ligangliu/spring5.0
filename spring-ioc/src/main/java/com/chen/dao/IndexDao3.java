package com.chen.dao;

import org.springframework.stereotype.Component;

/**
 * @Author liu
 * @Date 2019-10-20 17:01
 */
@Component
public class IndexDao3 {

	private IndexDao4 indexDao4;

	public IndexDao3(IndexDao4 indexDao4) {
		this.indexDao4 = indexDao4;
	}


}
