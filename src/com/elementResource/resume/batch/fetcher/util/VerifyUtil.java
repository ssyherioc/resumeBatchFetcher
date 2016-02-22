package com.elementResource.resume.batch.fetcher.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.httpclient.methods.MultipartPostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import com.elementResource.resume.batch.fetcher.context.SystemContext;


import net.sf.json.JSONObject;

@SuppressWarnings("deprecation")
public class VerifyUtil {

	protected static String DEFAULT_CAPTCHA = "1436";

	public static String getVerifyCode(HttpClient client, String website) {
		BufferedReader reader = null;
		MultipartPostMethod filePost = null;
		String verifyFilePath = null;
		try {
			try {
				verifyFilePath = getCaptcha(client, website);
			} catch (Exception e1) {
				return DEFAULT_CAPTCHA;
			} // 本地验证码地址
			if (verifyFilePath == null) {
				return DEFAULT_CAPTCHA;
			}

			// 获取验证码字符串值的地址：http://demo.cohirer.com/api/v2/recognize_51job
			String transURL = null;
			try {
				transURL = SystemContext.getConfigProperties(website).getProperty("verify_trans_url");
			} catch (Exception e1) {
			}
			filePost = new MultipartPostMethod(transURL);
			FilePart cbFile = new FilePart("upload", new File(verifyFilePath));
			cbFile.setContentType("image/*");
			filePost.addParameter("cmd_no", "2");
			filePost.addPart(cbFile);
			filePost.addParameter("pagesize", "10");
			filePost.addParameter("pageno", "1");
			org.apache.commons.httpclient.HttpClient apiClient = new org.apache.commons.httpclient.HttpClient();
			apiClient.executeMethod(filePost);
			// 由于要上传的文件可能比较大 , 因此在此设置最大的连接超时时间
			// client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
			reader = new BufferedReader(new InputStreamReader(filePost.getResponseBodyAsStream(), "utf-8"));
			StringBuffer response = new StringBuffer();
			String line;
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			reader.close();

			if (StringUtil.isNull(response.toString())) {
				return DEFAULT_CAPTCHA;
			} else {
				JSONObject jsonObj = JSONObject.fromObject(response.toString());
				if ("ok".equals(jsonObj.getString("result"))) {
					return jsonObj.getString("data");
				} else {
					return DEFAULT_CAPTCHA;
				}
			}
		} catch (Exception e) {
			return DEFAULT_CAPTCHA;
		} finally {
			if (verifyFilePath != null) {
				File imgFile = new File(verifyFilePath);
				if (imgFile.exists()) {
					imgFile.delete();
				}
			}
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
			if (null != filePost) {
				filePost.abort();
			}
		}

	}

	private static String getCaptcha(HttpClient client, String website) throws Exception {
		String fileName = null;
		String savePath = SystemContext.getConfigProperties(website).getProperty("verify_img_path");
		String urlPath = SystemContext.getConfigProperties(website).getProperty("url_login_on");
		if (Constant.WEBSITE_LIEPIN.equals(website)) {
	   		HttpGet get = new HttpGet(urlPath);
		    get.setHeader("charset","UTF-8");	
			get.setHeader("HTTP_REFERER", "http://c.liepin.com");
			HttpResponse response;
			try {
			    response = client.execute(get);
			    HttpEntity entity = response.getEntity();
			    String result = EntityUtils.toString(entity, "UTF-8");
			    int startIndex = result.lastIndexOf("/cap");
			    int endIndex = result.indexOf("\"", startIndex);
			    urlPath = "https://passport.liepin.com"+result.substring(startIndex, endIndex);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}		
			fileName = savePath + getCaptchaFileName(website);
		}

		if (null != urlPath) {
			HttpGet get = new HttpGet(urlPath);
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream is = entity.getContent();
				byte[] bs = new byte[1024];
				int len;
				File sf = new File(savePath);
				if (!sf.exists()) {
					sf.mkdirs();
				}
				OutputStream os = new FileOutputStream(fileName);
				while ((len = is.read(bs)) != -1) {
					os.write(bs, 0, len);
				}
				os.close();
				is.close();
			}
			get.abort();
		}
		return fileName;
	}

	private synchronized static String getCaptchaFileName(String website) {
		Date d = new Date();
		return  d.getTime() + ".jpg";
		//return website + "_" + d.getTime() + ".png";
	}
}
