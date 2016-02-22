/**
 * 
 */
package com.elementResource.resume.batch.fetcher.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com.elementResource.resume.batch.fetcher.context.SystemContext;


/**
 * @author qianeryu
 *
 */
public class FunctionUtil {

	public static Map<String, Object> getKeyValueListByFunction(String fid, String jsonName, String jsonValue) {
		Map<String, Object> kvMap = new HashMap<String, Object>();
		String functionEntity = SystemContext.getFunctionEntity(fid);
		
		if (functionEntity != null) {
			String functionURL = SystemContext.getFunctionURL();
			String functionValue = functionEntity.replaceAll("#expect_value#", jsonValue);
			String url = null;
			try {
				url = functionURL + URLEncoder.encode(functionValue, "utf-8");
			} catch (UnsupportedEncodingException e1) {}
			CloseableHttpClient client = null;
			HttpGet get = null;
			CloseableHttpResponse response = null;
			HttpEntity entity = null;
			try {
				client = HttpClients.createDefault();
				get = new HttpGet(url);
				client.execute(get);
				response = client.execute(get);
				entity = response.getEntity();
			} catch (Exception e) {
				kvMap.put(Constant.RTN_MSG_LEVEL_ERROR, "query function:" +fid+ " http request error:" + e.getMessage());
				return kvMap;
			}  finally {
				try {
					if (response != null) response.close();
					if (get != null) get.clone();
					if (client != null) client.close();
				} catch (Exception e) {}
			}
			
			String result = null;
			try {
				result = EntityUtils.toString(entity, "UTF-8");
			} catch (ParseException e) {
				kvMap.put(Constant.RTN_MSG_LEVEL_ERROR, "parse query function:" +fid+ " response error:" + e.getMessage());
			} catch (IOException e) {
				kvMap.put(Constant.RTN_MSG_LEVEL_ERROR, "parse query function:" +fid+ " response error:" + e.getMessage());
				e.printStackTrace();
			}
			//System.out.println(result);
			if (!StringUtil.isNull(result)) {
				JSONObject jsonResult = JSONObject.fromObject(result);
				int numSize = jsonResult.getInt("num_results");
				if (numSize <= 0) {
					kvMap.put(jsonName, jsonValue);
				} else {
					JSONArray jsonAry = jsonResult.getJSONArray("objects");
					JSONObject rsObj = (JSONObject) jsonAry.get(0);
					for (Iterator<String> iter = rsObj.keySet().iterator(); iter.hasNext();) {
						String key = iter.next();
						Object val = rsObj.get(key);
						kvMap.put(key, val);
					}
				}
				
			} else {
				kvMap.put(jsonName, jsonValue);
			}
		} else {
			kvMap.put(jsonName, jsonValue);
		}
		return kvMap;
	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 * @throws CloneNotSupportedException 
	 */
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException, CloneNotSupportedException {
		String s = new String("王后根".getBytes(), "utf-8");
		System.out.println(s);
	}

}
