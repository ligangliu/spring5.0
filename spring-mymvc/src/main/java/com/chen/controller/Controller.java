package com.chen.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author liu
 * @Date 2019-10-22 21:30
 */
@RestController
@RequestMapping("/zhang")
public class Controller {

	@RequestMapping("/test")
	public String test() {
		System.out.println("===========test=======");
		return "XXXXXX";
	}
}
