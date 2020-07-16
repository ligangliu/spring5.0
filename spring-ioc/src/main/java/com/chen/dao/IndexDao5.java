package com.chen.dao;

import org.springframework.beans.factory.SmartInitializingSingleton;

/**
 * @Author liu
 * @Date 2020-07-07 11:31
 */
public class IndexDao5 implements SmartInitializingSingleton {
	@Override
	public void afterSingletonsInstantiated() {
		System.out.println("indexDao5 ... afterSingletonsInstantiated");
	}
}
