package com.elementResource.resume.batch.fetcher.core.resume;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;

import net.sf.json.JSONObject;

import com.elementResource.resume.batch.fetcher.core.IBatchFetchResumeJson;
import com.elementResource.resume.batch.fetcher.core.resume.impl.BaseWebsite;
import com.elementResource.resume.batch.fetcher.core.resume.impl.FiveOneJobWebsite;
import com.elementResource.resume.batch.fetcher.core.resume.impl.LiepinWebsite;
import com.elementResource.resume.batch.fetcher.core.resume.impl.ZhaopinWebsite;
import com.elementResource.resume.batch.fetcher.util.Constant;
import com.elementResource.resume.batch.fetcher.util.MD5Util;
import com.elementResource.resume.batch.fetcher.util.StringUtil;
import com.elementResource.resume.batch.fetcher.context.SystemContext;

public class BatchFetchResumeContent implements IBatchFetchResumeJson{

	private String loginError;
	private String loginWarn;
	private String content;
	private List<String> contentList;
	
	@Override
	public String getCandIdListJsonByJobId(String website, String jobId, String candType,
			String compName, String compPWD) {
		ILoginWebsite loginWebsite = getWebsite(website);
		if(website.equals(Constant.WEBSITE_LIEPIN))
		{
			compPWD = MD5Util.MD5(compPWD);
		}
		loginWebsite.getCandIdListJsonByJobId(compName, compPWD, jobId, candType);
		Map<String, Object> resultMap= loginWebsite.getResultMap();
		
		splitLoginMessage(resultMap);
		Map<String, Object> jsonMap = null;
		if (!StringUtil.isNull(loginError)) {
			jsonMap = new LinkedHashMap<String, Object>();
			jsonMap.put(Constant.RTN_MSG_LEVEL_ERROR, loginError);
			jsonMap.put(Constant.RTN_MSG_LEVEL_WARN, loginWarn);
		}
		else {
			jsonMap = new WebInfoParser().getCandIdListJsonMap(website, content);
		}
		return map2Json(website, jsonMap);
	}

	@Override
	public String getCandInfoJsonByCandURL(String website, String candURL,
			String compName, String compPWD) {
		return null;
		/*ILoginWebsite loginWebsite = getWebsite(website);
		if(website.equals(Constant.WEBSITE_LIEPIN))
		{
			compPWD = MD5Util.MD5(compPWD);
		}
		loginWebsite.getJobListJson(compName, compPWD);
		Map<String, String> resultMap= loginWebsite.getResultMap();
		
		splitLoginMessage(resultMap);
		Map<String, Object> jsonMap = null;
		if (!StringUtil.isNull(loginError)) {
			jsonMap = new LinkedHashMap<String, Object>();
			jsonMap.put(Constant.RTN_MSG_LEVEL_ERROR, loginError);
			jsonMap.put(Constant.RTN_MSG_LEVEL_WARN, loginWarn);
		}
		else {
			jsonMap = new WebInfoParser().getJobListJsonMap(website, content);
		}
		return map2Json(website, jsonMap);*/
	}

	@Override
	public String getJobIdListJson(String website, String compName,
			String compPWD) {
		ILoginWebsite loginWebsite = getWebsite(website);
		if(website.equals(Constant.WEBSITE_LIEPIN))
		{
			compPWD = MD5Util.MD5(compPWD);
		}
		loginWebsite.getJobListJson(compName, compPWD);
		Map<String, Object> resultMap= loginWebsite.getResultMap();
		
		splitLoginMessage(resultMap);
		Map<String, Object> jsonMap = null;
		if (!StringUtil.isNull(loginError)) {
			jsonMap = new LinkedHashMap<String, Object>();
			jsonMap.put(Constant.RTN_MSG_LEVEL_ERROR, loginError);
			jsonMap.put(Constant.RTN_MSG_LEVEL_WARN, loginWarn);
		}
		else {
			jsonMap = new WebInfoParser().getJobListJsonMap(website, content);
		}
		return map2Json(website, jsonMap);
	}
	
	private void splitLoginMessage(Map<String, Object> resumeResultMap) {
		this.loginError = (String)resumeResultMap.get(BaseWebsite.ERROR);
		this.loginWarn = (String)resumeResultMap.get(BaseWebsite.WARNING);
		this.content = (String)resumeResultMap.get(BaseWebsite.CONTENT);
		this.contentList = (List<String>)resumeResultMap.get(BaseWebsite.CONTENTLIST);
	}
	
	public String map2Json(String website, Map<String, Object> jsonMap) {
		if (!isShowErrorMsg(website)) {
			jsonMap.remove(Constant.RTN_MSG_LEVEL_WARN);
		}
		JSONObject jsonObj = new JSONObject();
		jsonObj.putAll(jsonMap);
		String json = jsonObj.toString();
		json = json.replaceAll("\\\\u", "\\u");
		return json;
	}
	
	private boolean isShowErrorMsg(String website) {
		boolean isShow = false;
		try {
			String isShowStr = (String)SystemContext.getConfigProperties(website).get(Constant.IS_SHOW_ERROR_MSG);
			if (null != isShowStr && isShowStr.trim().toUpperCase().equals(Constant.TRUE_VALUE_STR)) {
				isShow = true;
			}
		} catch (Exception e) {}
		return isShow;
	}
	
	private ILoginWebsite getWebsite(String website){
		if(website.equals(Constant.WEBSITE_51JOB)){
			return new FiveOneJobWebsite(website);
		}else if(website.equals(Constant.WEBSITE_ZHAOPIN)){
			return new ZhaopinWebsite(website);
		}else if(website.equals(Constant.WEBSITE_LIEPIN)){
			return new LiepinWebsite(website);
		}
		else{
			return null;
		}
	}

	@Override
	public String getCandInfoListJsonByJobId(String website, String jobId,
			String candType, String compName, String compPWD) {
		ILoginWebsite loginWebsite = getWebsite(website);
		if(website.equals(Constant.WEBSITE_LIEPIN))
		{
			compPWD = MD5Util.MD5(compPWD);
		}
		loginWebsite.getCandIdListJsonByJobId(compName, compPWD, jobId, candType);
		Map<String, Object> resultMap= loginWebsite.getResultMap();
		
		splitLoginMessage(resultMap);
		Map<String, Object> jsonMap = null;
		if (!StringUtil.isNull(loginError)) {
			jsonMap = new LinkedHashMap<String, Object>();
			jsonMap.put(Constant.RTN_MSG_LEVEL_ERROR, loginError);
			jsonMap.put(Constant.RTN_MSG_LEVEL_WARN, loginWarn);
		}
		else {
			//获取所有resumeURL列表
			List<String> resumeURLList = new WebInfoParser().getCandURLList(website, content);
			loginWebsite.getCandInfoListJsonByURLList(compName, compPWD, resumeURLList);
			resultMap= loginWebsite.getResultMap();
			splitLoginMessage(resultMap);
			jsonMap = null;
			if (!StringUtil.isNull(loginError)) {
				jsonMap = new LinkedHashMap<String, Object>();
				jsonMap.put(Constant.RTN_MSG_LEVEL_ERROR, loginError);
				jsonMap.put(Constant.RTN_MSG_LEVEL_WARN, loginWarn);
			}
			else{
				//将所有HtmlContent进行拼装*/
				try {
					//jsonMap = new WebInfoParser().getCandInfoListJsonMap(website, contentList);
					jsonMap = new WebInfoParser().getCandInfoListJsonMap(website, contentList);
				} catch (DocumentException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return map2Json(website, jsonMap);
	}

}
