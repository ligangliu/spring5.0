package com.chen.dao;

import org.springframework.stereotype.Component;

/**
 * @Author liu
 * @Date 2020-07-12 14:08
 */
@Component
public class IndexDao6 {

	private IndexDao7 indexDao7;

	public IndexDao6(IndexDao7 indexDao7) {
		this.indexDao7 = indexDao7;
	}
}
