package com.chen.test;


/**
 * @Author liu
 * @Date 2019-11-02 13:04
 */
public class Spring {
	public static void main(String[] args) {
//		Logger logger = Logger.getLogger("spring");
//		logger.info("spring");
		try {
			Class.forName("xxxxx");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
