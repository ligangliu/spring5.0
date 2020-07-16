package com.chen.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author liu
 * @Date 2019-10-27 14:06
 */
@Component
public class MyHandlerMapping implements HandlerMapping {

	@Override
	public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
		return null;
	}
}
