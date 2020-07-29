package com.chen.dao;

import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Controller;

/**
 * @Author liu
 * @Date 2020-07-12 21:43
 */
@Controller
public class IndexDao9 {

	private String name;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
