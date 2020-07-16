package com.chen.app;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @Author liu
 * @Date 2020-07-08 9:16
 */
@Component
@Aspect
public class AspectChen {

	@Pointcut("execution(* com.chen.app.dao..*.*(..))")
	public void pointCut() {

	}

	@Before("pointCut()")
	public void before() {
		System.out.println("代理之前");
	}

	@After("pointCut()")
	public void after() {
		System.out.println("代理之后");
	}
}
