package com.doctorcyj._51job;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class Job_51 {
	
	public static String cookie;//登录需要的cookie
	public static Integer success = 0;//投递成功的计数
	public static Integer repetition = 0;//已重复投递的计数
	public static Integer error = 0;//投递失败的计数，网络错误等情况

	/**
	 * @param html 需要抓取的网址
	 * @return 返回一个网页的所有DIV数据
	 * @throws Exception
	 */
	public List<String> getAllDivData(String html) throws Exception{
		
		List<String> pageHtml = new ArrayList<>();
		boolean flag = false;//判断DIV开始与结束标志
		StringBuffer sbBuilder = new StringBuffer("");
		String tempHtml = "";
		URL url = new URL(html);
		HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
		//判断状态码是否是200
		if(httpConnection.getResponseCode()==200) {
//			System.out.println(httpConnection.getResponseCode());
			InputStream is = httpConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String temp = "";
			while((temp = br.readLine())!=null) {
				//判断网页源码字符集
				if(temp.contains("charset=")) {
					//截取网页编码类型
					int indexOf = temp.indexOf("charset=");
					temp = temp.substring(indexOf+8);
					indexOf = temp.lastIndexOf("\"");
					temp = temp.substring(0, indexOf);
					//重新设置读的编码类型
					isr = new InputStreamReader(is, temp);
					br = new BufferedReader(isr);
				}
				if(temp.trim().equalsIgnoreCase("<div class=\"el\">")) {
					flag = true;
				}
				//判断是否是DIV开始,是则添加到StringBuilder中
				if(flag) {
					//去除前后空格添加到StringBuilder中
					sbBuilder.append(temp.trim());
//					System.out.println(temp);
				}
				//判断是否是DIV结束
				if(temp.contains("</div>")) {
					flag = false;
					//将StringBuilder添加到list中
					pageHtml.add(sbBuilder.toString());
					//将StringBuilder清空
					sbBuilder = new StringBuffer("");
				}
			}
			is.close();
		}
		return pageHtml;
	}
	

	/**
	 * 返回一个网页中所有的职位描述 包括地址
	 * @param div_list DIV数据集合
	 * @return
	 */
	public List<String> getJob_pojo(List<String> div_list){
		
		List<String> list = new ArrayList<>();
		for (String string : div_list) {
			Job_pojo pojo = new Job_pojo();
			//截取网页链接
			int indexOf = string.indexOf("title=\"");
			if(indexOf!=-1) {
				String html = "";//链接地址
				String job = "";//职位名称
				string = string.substring(indexOf+7);
				job = string.substring(0, string.indexOf("\""));
//				System.out.println("工作职位名:"+job);
//				pojo.setJob_name(job);
				indexOf = string.indexOf("href=\"");
				if(indexOf != -1) {
					string = string.substring(indexOf+6);
					html = string.substring(0, string.indexOf("\""));
//					System.out.println("网页链接是:"+html);
//					pojo.setHtml(html);
					//添加网址
					list.add(html);
				}
			}
//			//截取公司名字
//			indexOf = string.indexOf("<span class=\"t2\"");
//			if(indexOf!=-1) {
//				String company_name = "";//公司名称
//				company_name = string.substring(string.indexOf("title=\"")+7);
//				company_name = company_name.substring(0, company_name.indexOf("\""));
////				System.out.println("公司名字是："+company_name);
//				pojo.setCompany_name(company_name);
//			}
//			//截取工作地点
//			indexOf = string.indexOf("<span class=\"t3\"");
//			if(indexOf != -1) {
//				String workplace = "";//工作地点
//				workplace = string.substring(indexOf);
//				//截取>号
//				workplace = workplace.substring(workplace.indexOf(">")+1);
//				//截取地点
//				workplace = workplace.substring(0, workplace.indexOf("<"));
////				System.out.println("工作地点是"+workplace);
//				pojo.setWorkplace(workplace);
//			}
//			//截取工资
//			indexOf = string.indexOf("<span class=\"t4\"");
//			if(indexOf != -1) {
//				String salary = "";
//				salary = string.substring(indexOf);
//				//截取>号
//				salary = salary.substring(salary.indexOf(">")+1);
//				//截取工资
//				salary = salary.substring(0, salary.indexOf("<"));
////				System.out.println("工资是："+salary);
//				pojo.setSalary(salary);
//			}
//			//截取发布日期
//			indexOf = string.indexOf("<span class=\"t5\"");
//			if(indexOf != -1) {
//				String time = "";
//				time = string.substring(indexOf);
//				//截取>号
//				time = time.substring(time.indexOf(">")+1);
//				//截取工资
//				time = time.substring(0, time.indexOf("<"));
////				System.out.println("发布日期是："+time);
//				pojo.setTime(time);
//			}
			//将一个工作的网页描述添加到集合中
//			list.add(pojo);
		}
		return list;
	}

	/**
	 * @param work 工作岗位
	 * @Param cityNumber城市代码
	 * @return 返回总页数,返回0代表链接错误或代码错误
	 * @throws Exception 
	 */
	public int getPageSize(String work,String cityNumber) throws Exception{
		
		//变成URL编码
		String urlString = URLEncoder.encode(work, "utf-8"); 
        urlString = URLEncoder.encode(urlString, "utf-8");
		String city = "040000";//城市编码，默认为深圳
		//修改城市编码
		if(cityNumber!=null&&cityNumber!="") {
			city = cityNumber;
		}
		String headPageHtml = "http://search.51job.com/list/"+city+",000000,0000,00,9,99,"+urlString+",2,1.html?lang=c&"
				+ "stype=&postchannel=0000&workyear=99&cotype=99&degr"
				+ "eefrom=99&jobterm=99&companysize=99&providesalary=9"
				+ "9&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&f"
				+ "romType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";
		URL url = new URL(headPageHtml);
		int num = 0;//记录总页数
		HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
		//判断状态码是否成功
		if(httpConnection.getResponseCode() == 200) {
//			System.out.println("状态码:"+httpConnection.getResponseCode());
			InputStream is = httpConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String temp = "";
			while((temp = br.readLine())!=null) {
				//判断网页源码字符集
				if(temp.contains("charset=")) {
					//截取网页编码类型
					int indexOf = temp.indexOf("charset=");
					temp = temp.substring(indexOf+8);
					indexOf = temp.lastIndexOf("\"");
					temp = temp.substring(0, indexOf);
					//重新设置读的编码类型
					isr = new InputStreamReader(is, temp);
					br = new BufferedReader(isr);
				}
				if(temp.contains("共")&&temp.contains("页")) {
					int i = temp.indexOf("共");
					String substring = "";
					if(i!=-1) {
						substring = temp.substring(i+1);
					}
					i = substring.indexOf("页");
					if(i!=-1) {
						substring = substring.substring(0,i);
						num = Integer.parseInt(substring);
					}
				}
				
			}
		}
		System.out.println("找到的总页数"+num);
		return num;
	}

	/**
	 * @param headHtml 第一页数据
	 * @param pageSize 总页数
	 * @return 根据第一页数据与总页数返回所有页数链接
	 * 返回的单个链接格式为：
	 * http://search.51job.com/list/040000,00
	 * 0000,0000,00,9,99,C,2,1.html?lang=c&stype
	 * =&postchannel=0000&workyear=99&cotype=99&de
	 * greefrom=99&jobterm=99&companysize=99&providesa
	 * lary=99&lonlat=0%2C0&radius=-1&ord_field=0&confi
	 * rmdate=9&fromType=&dibiaoid=0&address=&line=&specia
	 * larea=00&from=&welfare=
	 * @throws Exception 
	 */
	public List<String> getTotalListHtml(String headHtml,int pageSize) throws Exception{
	
		List<String> listHtml = new ArrayList<>();
		String html = "";
		//将第一页添加到集合中
//		listHtml.add(headHtml);
		//找出下一页链接
		for(int i =1;i<=pageSize;i++) {
			int indexOf = headHtml.indexOf(".html?");
			if(indexOf!=-1){
				String prefix = headHtml.substring(0, indexOf);//前缀
				String suffix = headHtml.substring(indexOf);//后缀
				int lastIndexOf = prefix.lastIndexOf(",");
				//截取了页码的前缀http://search.51job.com/list/040000,000000,0000,00,9,99,java,2,
				prefix = prefix.substring(0,indexOf-1);
				html = prefix+i+suffix;
				listHtml.add(html);
			}
		}
		return listHtml;
	}
	
	/**
	 * 模拟登陆获取登录Cookie
	 * @param username 登录账户
	 * @param password 密码
	 * @return
	 * @throws Exception
	 */
	public String loginGetCookie(String username,String password) throws Exception{ 
		HttpEntity entity = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();   
    	HttpPost httpPost = new HttpPost("https://login.51job.com/login.php?lang=c&cat=51260020&s=119&sort=s&style=g&search_condition=48&from=sn_1_cat&active=1&new=1&shopType=any&industryCatId=51260020&type=pc#J_Filter");
    	httpPost.setHeader("User-Agent", " Mozilla/5.0 (Windows NT 10.0; WOW64; rv:58.0) Gecko/20100101 Firefox/58.0");
    	httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
    	httpPost.setHeader("Host", "login.51job.com");
    	httpPost.setHeader("Referer", "https://login.51job.com/login.php?lang=c");
    	httpPost.setHeader("Accept-Language","zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
    	//设置post表单数据
    	List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>(); 
    	pairList.add(new BasicNameValuePair("lang", "c"));
        pairList.add(new BasicNameValuePair("action", "save"));
        pairList.add(new BasicNameValuePair("from_domain", "i"));
        pairList.add(new BasicNameValuePair("loginname", username));//设置账户
        pairList.add(new BasicNameValuePair("password", password));//设置密码
        pairList.add(new BasicNameValuePair("verifycode", ""));
        pairList.add(new BasicNameValuePair("isread", "on"));
        //添加请求体
        httpPost.setEntity(new UrlEncodedFormEntity(pairList, "utf-8"));
    	// 执行请求.     
        HttpClientContext context = HttpClientContext.create();//创建http上下文
        CloseableHttpResponse response = httpclient.execute(httpPost, context);
        CookieStore cookieStore = context.getCookieStore();
        List<Cookie> cookies = cookieStore.getCookies();
        httpclient.close();
        //获取Cookie名字与值
        StringBuilder sBuilder = new StringBuilder();
        for (Cookie cookie : cookies) {
        	String name = cookie.getName();
        	String value = cookie.getValue();
        	sBuilder.append(name+"="+value+";");
		}
        return sBuilder.toString();
	}
	
	
	/**
	 * @param cookie 需要提交的cookie
	 * @param jobHtml 源地址，51job防止盗链，含有公司ID
	 * @throws Exception
	 */
	public void sendResume(String cookie,String jobHtml) throws Exception{

		String id = jobHtml.substring(jobHtml.lastIndexOf("/")+1, jobHtml.lastIndexOf("."));
		HttpEntity entity = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();   
		HttpGet httpget = new HttpGet("https://i.51job.com//delivery/"
    			+ "delivery.php?rand=0.92359197063203176&jobid=("+id+"%3A0)"
    			+ "&prd=&prp=01&cd=jobs.51job.com&cp=0"
    			+ "1&resumeid=&cvlan=&coverid=&qpostset=&elementnam"
    			+ "e=hidJobID&deliverytype=1&deliverydomain=%2F%2Fi."
    			+ "51job.com%2F&language=c&imgpath=https%3A%2F%2Fim"
    			+ "g06.51jobcdn.com%2F");
    	httpget.setHeader("User-Agent", " Mozilla/5.0 (Windows NT 10.0; "
    			+ "WOW64; rv:58.0) Gecko/20100101 Firefox/58.0");
    	httpget.setHeader("Host", "Host: i.51job.com");
    	//源地址
    	httpget.setHeader("Referer", jobHtml);
    	httpget.setHeader("Accept-Language","zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
    	httpget.setHeader("Accept","*/*");
    	httpget.setHeader("Cookie", cookie);
    	CloseableHttpResponse response = httpclient.execute(httpget);
    	entity = response.getEntity();
    	String string = EntityUtils.toString(entity, "utf-8");
    	//关闭资源
    	httpclient.close();
    	if(string.contains("投递成功")){
    		synchronized (Demo.class) {
    			Job_51.success+=1;
    		}
    		System.out.println("投递成功"+Job_51.success);
    	}else if(string.contains("7天")){
    		synchronized (Demo.class) {
    			Job_51.repetition+=1;
			}
    		System.out.println("已经投递过了"+Job_51.repetition);
    	}else{
    		synchronized (Demo.class) {
    			Job_51.error+=1;
			}
    		System.out.println("网络错误或地址错误，投递失败"+Job_51.error);
    	}
	}
}
