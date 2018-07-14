package com.doctorcyj._51job;

import java.util.List;
import java.util.Scanner;

import org.junit.Test;

public class Demo {

	public static void main(String[] args) throws Exception {
		
		Job_51 job = new Job_51();
		//获取登录cookie
		String cookie = job.loginGetCookie("abc", "abc");
		Job_51.cookie = cookie;
//		System.out.println(cookie);
		//获取总页数的连接地址
//		List<String> listHtml = job.getTotalListHtml("https://search.51job.com/list/040000,0"
//				+ "00000,0000,00,9,99,java,2,1.html?lang=c&stype=&po"
//				+ "stchannel=0000&workyear=99&cotype=99&degreefrom=9"
//				+ "9&jobterm=99&companysize=99&providesalary=99&lonlat"
//				+ "=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType"
//				+ "=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=", 200);
//		//遍历地址，并发送
//		for (String html : listHtml) {
//			new SendResumeThread(html).start();
//		}
	}

}
