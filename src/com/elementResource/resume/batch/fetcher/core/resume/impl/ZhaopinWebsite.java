package com.elementResource.resume.batch.fetcher.core.resume.impl;

import org.apache.http.client.HttpClient;

public class ZhaopinWebsite extends BaseWebsite {

	public ZhaopinWebsite(String website) {
		super(website);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean checkLogin(String loginResult, HttpClient client,
			String user, String password, int tryCount, String captcha) {
		// TODO Auto-generated method stub
		
		if(loginResult.indexOf("您输入的密码与账号不匹配")!=-1){
			put2RtnMap(ERROR, "login fail! Username or password is wrong!");
			return false;
		}else if(loginResult.indexOf("手机号或邮箱不存在，请确认后重新输入！")!=-1){
			put2RtnMap(ERROR, "login fail! Username is not exists!");
			return false;
		}else if(loginResult.indexOf("请输入正确的验证码")!=-1){
			put2RtnMap(ERROR, "login fail! verify code is wrong!");
			return false;	
		}else{
			return true;
		}
		
	}

}
