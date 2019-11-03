package com.chen.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author liu
 * @Date 2019-10-18 16:23
 */
@Component
public class IndexDao1 {

	@Autowired
	private IndexDao2 indexDao2;

	public void query() {
		System.out.println("query");
	}
}
