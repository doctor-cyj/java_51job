package com.doctorcyj._51job;

public class Job_pojo {

	private String job_name; //职位名
	private String company_name; //公司名
	private String workplace;//工作地点
	private String salary;//工资范围
	private String time;//发布时间
	private String html;//网页链接地址
	
	
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public String getJob_name() {
		return job_name;
	}
	public void setJob_name(String job_name) {
		this.job_name = job_name;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getWorkplace() {
		return workplace;
	}
	public void setWorkplace(String workplace) {
		this.workplace = workplace;
	}
	public String getSalary() {
		return salary;
	}
	public void setSalary(String salary) {
		this.salary = salary;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "Job_pojo [job_name=" + job_name + ", company_name=" + company_name + ", workplace=" + workplace
				+ ", salary=" + salary + ", time=" + time + ", html=" + html + "]";
	}
	
	
}
