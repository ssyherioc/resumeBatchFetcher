package com.elementResource.resume.batch.fetcher.core.resume.impl;

import org.apache.http.client.HttpClient;

import com.elementResource.resume.batch.fetcher.util.StringUtil;
import com.elementResource.resume.batch.fetcher.util.VerifyUtil;


public class FiveOneJobWebsite extends BaseWebsite {

	public FiveOneJobWebsite(String website) {
		super(website);
		// TODO Auto-generated constructor stub
	}

	private int getCode(String result, String key) {
		int i = result.indexOf(key + "\":");
		int j = result.indexOf(",", i);
		return Integer.parseInt(result.substring(i + key.length() + 2, j));
	}
	
	@Override
	protected boolean checkLogin(String loginResult,HttpClient client,String user, String password, int tryCount, String captcha){
		// TODO Auto-generated method stub
		if (StringUtil.isNull(loginResult)) {
			// Login success
			return true;
		}

		int result = getCode(loginResult, "result"), status = getCode(loginResult, "status");

		if (result == 1 && status == 1) {
			// Login success
			return true;
		}else if (result == 0 && status == -2) {
			// Password Error
			put2RtnMap(ERROR, "login fail! Username or password is wrong!");
			return false;
		}else if (result == 0 && (status == -3 || status == -1)) {
			// status(-3): Need captcha verification status(-1): Wrong
			// captcha
			captcha = VerifyUtil.getVerifyCode(client, website);
			return login(client, user, password, tryCount + 1, captcha);
		}else{
			return true;
		}
	}

}
