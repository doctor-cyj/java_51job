package com.doctorcyj._51job;

import java.util.List;

/**
 * 一个线程执行一页的职位发送
 * @author Administrator
 *
 */
public class SendResumeThread extends Thread {

	private String html;//一页的职位的地址
	private Job_51 job_51;
	/**
	 * 
	 * @param html 
	 * @param
	 */
	public SendResumeThread(String html) {
		job_51 = new Job_51();
		this.html = html;
	}
	
	@Override
	public void run(){
		
		try {
			//获取所有DIV
			List<String> divList = job_51.getAllDivData(html);
			//获取所有职位连接
			List<String> job_html = job_51.getJob_pojo(divList);
			//遍历职位连接
			for (String html : job_html) {
				job_51.sendResume(Job_51.cookie, html);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
