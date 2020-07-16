package com.chen.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author liu
 * @Date 2019-10-22 21:30
 */
@org.springframework.stereotype.Controller
public class Controller {

	@RequestMapping("/index.do")
	@ResponseBody
	public String index() {
		System.out.println("===========index=======");
		return "XXXXXX";
	}

//	@RequestMapping("/index2.do")
//	@ResponseBody
//	public Map<String, String> index2() {
//		System.out.println("===========index2=======");
//		Map<String, String> map = new HashMap<>();
//		map.put("a", "aaaa");
//		return map;
//	}

	@RequestMapping("/upload")
	public void uplaod(@RequestPart("zilu") MultipartFile multipart){
		System.out.println("aaa");
	}

}
