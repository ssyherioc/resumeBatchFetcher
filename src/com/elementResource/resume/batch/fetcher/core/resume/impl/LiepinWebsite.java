package com.elementResource.resume.batch.fetcher.core.resume.impl;

import org.apache.http.client.HttpClient;

import com.elementResource.resume.batch.fetcher.util.VerifyUtil;

public class LiepinWebsite extends BaseWebsite {

	public LiepinWebsite(String website) {
		super(website);
		// TODO Auto-generated constructor stub
	}

	private int getCode(String result, String key, Boolean isNum) {
		int i = result.indexOf(key + "\":");
		int j = result.indexOf(",", i);
		if(j==-1){
			j = result.indexOf("}");
		}
		if(isNum){
			return Integer.parseInt(result.substring(i + key.length() + 2, j));
		}
		else{
			return Integer.parseInt(result.substring(i + key.length() + 3, j-1));
		}
	}
	
	private int getErrCount(String result, String key) {
		int i = result.indexOf(key + "\":");
		int j = result.indexOf("}", i);
		String err = result.substring(i+key.length()+3,j-1);
		if(err.equals("系统检测该帐号操作异常，自动锁定2小时")){
			return 0;
		}
		else{			
			err = err.substring(err.indexOf("次机会")-1,err.indexOf("次机会"));
			return Integer.parseInt(err);
		}
	}
	
	@Override
	protected boolean checkLogin(String loginResult, HttpClient client,
			String user, String password, int tryCount, String captcha) {		
		//系统检测该帐号操作异常，自动锁定2小时
		int flag = getCode(loginResult, "flag",true);
		if(flag == 1){
			return true;
		}
		else if(flag == 0){
			if(loginResult.contains("验证码")||loginResult.contains("请填写必要信息")){		   
				captcha = VerifyUtil.getVerifyCode(client,website);
				return login(client, user, password, tryCount + 1, captcha);
			}
			else{
				put2RtnMap(ERROR, String.format("login fail! Remaining: %d", getErrCount(loginResult,"err")));
				return false;
			}
		}
		else{
			return false;
		}
		
		
	}

}
