package com.chen.cglib;

import org.springframework.cglib.proxy.Enhancer;

/**
 * @Author liu
 * @Date 2019-10-20 9:11
 */
public class Test {

	public static void main(String[] args) {
		//使用Enhancer创建代理类
		Enhancer enhancer = new Enhancer();
		//继承被代理类
		enhancer.setSuperclass(Cglib.class);
		//设置回调
		enhancer.setCallback(new CglibProxy());

		//生成代理对象
		Cglib cglib = (Cglib) enhancer.create();

		cglib.query();
	}

	static class Cglib {
		public void query() {
			System.out.println("cglib query....");
		}
	}
}
