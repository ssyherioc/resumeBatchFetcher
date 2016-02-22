package com.elementResource.resume.batch.fetcher.core.resume.impl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import com.elementResource.resume.batch.fetcher.context.SystemContext;
import com.elementResource.resume.batch.fetcher.core.resume.ILoginWebsite;
import com.elementResource.resume.batch.fetcher.util.Constant;
import com.elementResource.resume.batch.fetcher.util.StringUtil;

public abstract class BaseWebsite implements ILoginWebsite {

	public static final String WARNING = "WARNING";
	public static final String ERROR = "ERROR";
	public static final String CONTENT = "CONTENT";
	public static final String CONTENTLIST = "CONTENTLIST";

	private static String PARAM_USER = "user_login";
	private static String PARAM_PWD = "user_pwd";
	private static String CHARSET = "UTF-8";
	private static String URL_LOGIN_SERVER = "https://passport.liepin.com/e/login.json";
	private static String URL_JOB_LIST = "https://lpt.liepin.com/ejob/showPublishEjobList/";
	private static String URL_CAND_LIST = "https://lpt.liepin.com/apply/ejob/showRecvResumeList/?kind=2&ejob_id=%s&catetory=%s&curPage=%d";
	private static String URL_GETID_URL= "https://lpt.liepin.com";
	private static String PARAM_CAPTCHA = "verifycode";
	private static String CUR_PAGE = "cur_page_attr";
	
	public static boolean INIT_FLAG = false;

	public static int RETRY_COUNT = 10;

	protected String website;
	private Map<String, Object> rtnMap = new HashMap<String, Object>();

	public BaseWebsite(String website) {
		this.website = website;
	}

	/**
	 * 
	 */
	public void init() {
		if (INIT_FLAG == false) {
			Properties properties = null;
			try {
				properties = SystemContext.getConfigProperties(website);
			} catch (Exception e) {
				put2RtnMap(WARNING, "load " + website + ".properties fail, then use the default setting.");
				e.printStackTrace();
			}
			PARAM_USER = properties.getProperty("username_attr_name");
			PARAM_PWD = properties.getProperty("password_attr_name");
			CHARSET = properties.getProperty("charset");
			URL_LOGIN_SERVER = properties.getProperty("url_login_server");
			URL_JOB_LIST = properties.getProperty("url_job_list");
			URL_CAND_LIST = properties.getProperty("url_cand_list");
			URL_GETID_URL = properties.getProperty("url_getid_uri");
			PARAM_CAPTCHA = properties.getProperty("url_login_verify");	
			CUR_PAGE = properties.getProperty("cur_page_attr");
			String retryCntStr = properties.getProperty(Constant.RETRY_COUNT_ON_VERIFY);
			if (!StringUtil.isNull(retryCntStr)) {
				RETRY_COUNT = Integer.parseInt(retryCntStr);
			}

		}
	}

	protected void put2RtnMap(String type, String msg) {
		String everMsg = (String) rtnMap.get(type);
		if (everMsg == null) {
			rtnMap.put(type, msg);
		} else {
			rtnMap.put(type, everMsg + "     " + msg);
		}
	}

	abstract protected boolean checkLogin(String loginResult,HttpClient client,String user, String password, int tryCount, String captcha);

	protected boolean login(HttpClient client, String user, String password, int tryCount, String captcha) {

		// Fail if tried more than 10 times
		if (tryCount > RETRY_COUNT)
			return false;

		HttpPost post = null;
		String loginResult = "";

		List<NameValuePair> loginParams = new ArrayList<NameValuePair>();
		if(website.equals(Constant.WEBSITE_LIEPIN)){
			//隐藏输入项
			loginParams.add(new BasicNameValuePair("user_kind", "1"));
			loginParams.add(new BasicNameValuePair("url", ""));
		}
		loginParams.add(new BasicNameValuePair(PARAM_USER, user));
		loginParams.add(new BasicNameValuePair(PARAM_PWD, password));
		if (!StringUtil.isNull(captcha))
			loginParams.add(new BasicNameValuePair(PARAM_CAPTCHA, captcha));
		try {
			post = new HttpPost(URL_LOGIN_SERVER);
			if(website.equals(Constant.WEBSITE_LIEPIN)){
				//header
				post.setHeader("Accept", "application/json, text/javascript; q=0.01");
				post.setHeader("Accept-Language", "zh-CN");
				post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
				post.setHeader("X-Requested-With", "XMLHttpRequest");
			}
			HttpEntity he = new UrlEncodedFormEntity(loginParams);
			post.setEntity(he);
			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			int httpStatus = response.getStatusLine().getStatusCode();
			if (HttpStatus.SC_OK != httpStatus) {
				put2RtnMap(ERROR, "login status error! status=" + httpStatus);
				return false;
			}
			loginResult = EntityUtils.toString(entity, CHARSET);
			//System.out.println(loginResult);
			return checkLogin(loginResult,client,user,password,tryCount,captcha);
		} catch (Exception e) {
			put2RtnMap(ERROR, "login fail! please check: " + e);
			if (post != null) {
				post.abort();
			}
			e.printStackTrace();
		} finally {
			if (post != null) {
				post.abort();
			}
		}
		return false;
	}

	private boolean doLogin(String user, String password, HttpClient client) {
		try {
			return login(client, user, password, 0, null);
		} catch (Exception e) {
			e.printStackTrace();
			put2RtnMap(ERROR, "Catch a Exception on login, please check log.");
			return false;
		}
	}
	

	/** 
     * 方法一 
     * @param clientBuilder 
     */  
    public static void configureHttpClient(HttpClientBuilder clientBuilder)   
    {  
        try  
        {  
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy()  
            {  
                // 信任所有  
				@Override
				public boolean isTrusted(X509Certificate[] arg0, String arg1)
						throws CertificateException {
					return true;
				}  
            }).build();  
              
            clientBuilder.setSSLContext(sslContext);  
              
        }catch(Exception e)  
        {  
            e.printStackTrace();  
        }  
    }  
	
    public void getJobListJson(String userName, String pwd){
    	init();
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();        
        configureHttpClient(httpClientBuilder);  
        HttpClient client = httpClientBuilder.build();
        if(!doLogin(userName, pwd, client)){
        	return;
        }
        String jobListURL = URL_JOB_LIST ;
		// System.out.println("MY CENTER URL:" + myCenterURL);
		String jobListHtml = null;
		try {
			HttpGet get = new HttpGet(jobListURL);
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			jobListHtml = EntityUtils.toString(entity, CHARSET);
			//System.out.println("jobListHtml=" + jobListHtml);
			
			get.abort();
		} catch (Exception e) {
			put2RtnMap(ERROR, "Get job list catch a exception, please check log.");
		}	
		int page = 1;
		if(website.equals(Constant.WEBSITE_LIEPIN)){
			int startIndex = jobListHtml.indexOf("tfoot");
			int endIndex = jobListHtml.indexOf("/tfoot");
			String tmp = jobListHtml.substring(startIndex,endIndex);
			startIndex = tmp.indexOf("分");
			endIndex = tmp.indexOf("页显示", startIndex);
			page = Integer.parseInt(tmp.substring(startIndex+"分".length(), endIndex));
			if(page > 1){
				for(int i=1;i<page;i++){
					try {
						HttpGet get = new HttpGet(jobListURL+"?"+CUR_PAGE+"="+i);
						HttpResponse response = client.execute(get);
						HttpEntity entity = response.getEntity();
						tmp = EntityUtils.toString(entity, CHARSET);
						get.abort();
					} catch (Exception e) {
						put2RtnMap(ERROR, "Get job list catch a exception, please check log.");
					}
					jobListHtml = jobListHtml+tmp;
				}
			}
		}
		put2RtnMap(CONTENT, jobListHtml);
    }
    
    public void getCandIdListJsonByJobId(String userName, String pwd, String jobId, String candType){
    	init();
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();        
        configureHttpClient(httpClientBuilder);  
        HttpClient client = httpClientBuilder.build();
        if(!doLogin(userName, pwd, client)){
        	return;
        }
        String candTypeTrans = transferCandType(candType);
        String candIdListURL = String.format(URL_CAND_LIST,jobId,candTypeTrans,0);
		// System.out.println("MY CENTER URL:" + myCenterURL);
		String candIdListHtml = null;
		try {
			HttpGet get = new HttpGet(candIdListURL);
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			candIdListHtml = EntityUtils.toString(entity, CHARSET);
			System.out.println("jobListHtml=" + candIdListHtml);
			
			get.abort();
		} catch (Exception e) {
			put2RtnMap(ERROR, "Get job list catch a exception, please check log.");
		}	
		
		int page = 1;
		if(website.equals(Constant.WEBSITE_LIEPIN)){
			int startIndex = candIdListHtml.indexOf("tfoot");
			int endIndex = candIdListHtml.indexOf("/tfoot");
			String tmp = candIdListHtml.substring(startIndex,endIndex);
			startIndex = tmp.indexOf("分");
			endIndex = tmp.indexOf("页显示", startIndex);
			page = Integer.parseInt(tmp.substring(startIndex+"分".length(), endIndex));
			if(page > 1){
				for(int i=1;i<page;i++){
					try {
						HttpGet get = new HttpGet(String.format(URL_CAND_LIST,jobId,candTypeTrans,i));
						HttpResponse response = client.execute(get);
						HttpEntity entity = response.getEntity();
						tmp = EntityUtils.toString(entity, CHARSET);
						get.abort();
					} catch (Exception e) {
						put2RtnMap(ERROR, "Get candId list catch a exception, please check log.");
					}
					candIdListHtml = candIdListHtml+tmp;
				}
			}
		}
		put2RtnMap(CONTENT, candIdListHtml);
    }
    
    public void getCandInfoListJsonByURLList(String userName, String pwd, List<String> urlList){
    	init();
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();        
        configureHttpClient(httpClientBuilder);  
        HttpClient client = httpClientBuilder.build();
        if(!doLogin(userName, pwd, client)){
        	return;
        }
        List<String> contentList = new ArrayList<String>();
        String htmlContent = null;
        for(String urlStr:urlList){
        	int index1 = urlStr.indexOf("&apply_time");
        	int index2 = urlStr.indexOf("&", index1+1);
        	urlStr = urlStr.substring(0,index1)+urlStr.substring(index2+1);
        	urlStr = URL_GETID_URL + urlStr;
			try {
				HttpGet get = new HttpGet(urlStr);
				HttpResponse response = client.execute(get);
				HttpEntity entity = response.getEntity();
				htmlContent = EntityUtils.toString(entity, CHARSET);
				get.abort();
			} catch (Exception e) {
				put2RtnMap(ERROR, "Get candId list catch a exception, please check log.");
			}
			contentList.add(htmlContent);
        }
        rtnMap.put(CONTENTLIST, contentList);
    }
    
	/*public void getResume(String userName, String pwd) {
		init();
		String result = "";
		
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();  
        
        configureHttpClient(httpClientBuilder);  
          
        HttpClient client = httpClientBuilder.build();
		doLogin(userName, pwd, client);// 登陆cookie id
		// 进入个人中心
		String myCenterURL = MY_CENTER;
		// System.out.println("MY CENTER URL:" + myCenterURL);
		String myCenterHtml = null;
		try {
			HttpGet get = new HttpGet(myCenterURL);
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			myCenterHtml = EntityUtils.toString(entity, CHARSET);
			System.out.println("myCenterHtml=" + myCenterHtml);
			get.abort();
		} catch (Exception e) {
			e.printStackTrace();
			put2RtnMap(ERROR, "Get personal center Content catch a exception, please check log.");
		}
		try {
			if (!StringUtil.isNull(myCenterHtml)) {
				result = getResumeContent(client, myCenterHtml);
				put2RtnMap(CONTENT, result); // 把内容加到map中
			} else {
				//
				put2RtnMap(ERROR, "Get personal center Content fail, the content is empty.");
			}
		} catch (Exception e) {
			put2RtnMap(ERROR, "Get personal resume content catch a exception.");
		}
	}

	private String getResumeContent(HttpClient client, String myCenterHtml) throws Exception {
		String result = "";

		String url = getResumeEditLink(myCenterHtml);
		if (url == null) {
			put2RtnMap(ERROR, "Can not get resume edit page link url! ");
			return null;
		}
		// System.out.println("resume content URL=" + url);
		HttpGet get = new HttpGet(url);
		if(this.website.equals(Constant.WEBSITE_LIEPIN)){
			get.setHeader("charset","UTF-8");	
			get.setHeader("HTTP_REFERER", "http://c.liepin.com");
		}
		else{
			get.setHeader("Content-Type", "text/html; charset=gb2312");
			get.setHeader("robots", "all");
			get.setHeader("Expires", "0");
			get.setHeader("Cache-Control", "no-cache");
			get.setHeader("Pragma", "no-cache");
			// get.setHeader("X-UA-Compatible", "IE=EmulateIE7");
			//get.setHeader("HTTP_REFERER", "http://my.51job.com/my/My_Pmc.php");
		}
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();
		result = EntityUtils.toString(entity, CHARSET);
		get.abort();

		return result;
	}

	private String getResumeEditLink(String html) {
		Document doc = Jsoup.parse(html);
		Elements links = doc.select("a");
		for (int i = 0; i < links.size(); i++) {
			Element link = links.get(i);
			String href = link.attr("href");
			if (href != null && href.indexOf(RESUME_URL) != -1) {
				if(this.website.equals(Constant.WEBSITE_LIEPIN)){
					href = "http://c.liepin.com"+href;
				}
				return href;
			}
		}
		return null;
	}*/

    public String transferCandType(String candType) {
		if(candType.equals(Constant.CAND_TYPE_UNDEAL)){
			return "0";
		}
		else if (candType.equals(Constant.CAND_TYPE_TARGET)) {
			return "1";
		}
		else if (candType.equals(Constant.CAND_TYPE_UNSUIT)) {
			return "2";
		}
		else if (candType.equals(Constant.CAND_TYPE_UNDETERMINED)) {
			return "3";
		}
		else if (candType.equals(Constant.CAND_TYPE_FILTER)) {
			return "4";
		}
		else{
			return null;
		}
	}
    
	public Map<String, Object> getResultMap() {
		return this.rtnMap;
	}

}
