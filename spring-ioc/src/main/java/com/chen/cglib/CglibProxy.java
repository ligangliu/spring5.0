package com.chen.cglib;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author liu
 * @Date 2019-10-20 9:09
 */
public class CglibProxy implements MethodInterceptor {
	@Override
	public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
		System.out.println("代理前。。。。。。。");
		Object o1 = methodProxy.invokeSuper(o, objects);
		System.out.println("代理后。。。。。");
		return o1;
	}
}
