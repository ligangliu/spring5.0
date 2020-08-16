package com.chen.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author liu
 * @Date 2020-07-26 17:05
 */
@Component
public class IndexDao12 {

	@Autowired
	private Student3 student4;
//
//	public IndexDao12() {
//		System.out.println("无参构造");
//	}

	@Value("nihao")
	private String name;

	@Value("2")
	private int age;

//	public IndexDao12(IndexDao9 indexDao9) {
//		System.out.println("==================================");
//		System.out.println(indexDao9);
//		System.out.println("==================================");
//	}

//	public IndexDao12(IndexDao8 indexDao8, IndexDao9 indexDao9) {
//		System.out.println("**************************************");
//		System.out.println(indexDao8);
//		System.out.println(indexDao9);
//		System.out.println("**************************************");
//	}

//	@Value("xxx")
//	@Autowired
	public void test(IndexDao9 indexDao9) {
		System.out.println("=================================");
		System.out.println(indexDao9);
		System.out.println(name + "   " + age);
		System.out.println("---------------------------------");
	}

	public void setIndexDao9(IndexDao9 indexDao9) {
		System.out.println("set111111111");
	}

	public void setIndexDao(IndexDao indexDao) {
		System.out.println("set222222222");
	}

	public void setName1(IndexDao9 indexDao9) {
		System.out.println("fffffffffffffffffffffff");
		System.out.println(indexDao9);
		System.out.println("fffffffffffffffffffffff");
	}


	public void setXXXX(IndexDao9 indexDao9) {
		System.out.println("_____________________");
		System.out.println("_____________________");
	}


}
