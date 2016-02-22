package com.elementResource.resume.batch.fetcher.core.resume;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Attribute;
import org.dom4j.DocumentException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.elementResource.resume.batch.fetcher.context.SystemContext;
import com.elementResource.resume.batch.fetcher.entity.JsonElement;
import com.elementResource.resume.batch.fetcher.fetchValueRule.IRule;
import com.elementResource.resume.batch.fetcher.filter.JsoupContentFilter;
import com.elementResource.resume.batch.fetcher.filter.JsoupKeywordFilter;
import com.elementResource.resume.batch.fetcher.util.Constant;
import com.elementResource.resume.batch.fetcher.util.DataTypeUtil;
import com.elementResource.resume.batch.fetcher.util.FetchValueRuleUtil;
import com.elementResource.resume.batch.fetcher.util.FunctionUtil;
import com.elementResource.resume.batch.fetcher.util.StringUtil;
import com.elementResource.resume.batch.fetcher.util.UnicodeUtil;




public class WebInfoParser{
	
	private List<String> errorMsgList = new ArrayList<String>();
	
	private List<String> warnMsgList = new ArrayList<String>();
	
	private Map<String, Object> rootJsonMap = new LinkedHashMap<String, Object>();
	
	private String webSite;
	
	private String currParseNode;
	
	public Map<String, Object> getJobListJsonMap(String website, String htmlContent){
	
		webSite = website;
		
		if(website.equals(Constant.WEBSITE_LIEPIN)){
			getJobList(htmlContent);
		}
		
		rootJsonMap.put(Constant.RTN_MSG_LEVEL_ERROR, errorMsgList);
		rootJsonMap.put(Constant.RTN_MSG_LEVEL_WARN, warnMsgList);
		
		return rootJsonMap;
	}
	
	public Map<String, Object> getCandIdListJsonMap(String website, String htmlContent){
		
		webSite = website;
		
		if(website.equals(Constant.WEBSITE_LIEPIN)){
			getCandIdList(htmlContent);
		}
		
		rootJsonMap.put(Constant.RTN_MSG_LEVEL_ERROR, errorMsgList);
		rootJsonMap.put(Constant.RTN_MSG_LEVEL_WARN, warnMsgList);
		
		return rootJsonMap;
	}
	
	public List<String> getCandURLList(String website, String htmlContent){
		List<String> resumeURLList = new ArrayList<String>();
		webSite = website;	
		try {
			if(website.equals(Constant.WEBSITE_LIEPIN)){
				String resumeURLAttr;
				resumeURLAttr = SystemContext.getConfigProperties(webSite).getProperty("data_resume_url");
				resumeURLList = getInfoList(htmlContent, resumeURLAttr);
			}
		} catch (DocumentException e) {
			errorMsgList.add(e.toString());
		} catch (IOException e) {
			errorMsgList.add(e.toString());
		}
		return resumeURLList;
	}

	private void getCandIdList(String htmlContent) {
    	try{
			String resumeIdAttr = SystemContext.getConfigProperties(webSite).getProperty("data_resume_id");
			String resumeURLAttr = SystemContext.getConfigProperties(webSite).getProperty("data_resume_url");
			String userCNameAttr = SystemContext.getConfigProperties(webSite).getProperty("data_usercName");
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			List<String> resumeIdList = getInfoList(htmlContent, resumeIdAttr);
			List<String> resumeURLList = getInfoList(htmlContent, resumeURLAttr);
			List<String> userCNameList = getInfoList(htmlContent, userCNameAttr);				
			for(int i=0;i<resumeIdList.size();i++){
				Map<String, Object> mapObject = new LinkedHashMap<String, Object>();
				mapObject.put("resumeId", resumeIdList.get(i));
				mapObject.put("resumeURL", resumeURLList.get(i));
				mapObject.put("userCName", userCNameList.get(i));
				list.add(mapObject);
			}
			rootJsonMap.put("CandIdList", list);
    	}
    	catch (Exception e) {
			errorMsgList.add(e.toString());
		}
		
	}

	private void getJobList(String jobListHtml){	
    	try{
			String jobIdAttr = SystemContext.getConfigProperties(webSite).getProperty("job_id_attr");
			String jobTitleAttr = SystemContext.getConfigProperties(webSite).getProperty("job_title_attr");
			String jobDqAttr = SystemContext.getConfigProperties(webSite).getProperty("job_dq_attr");
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			List<String> jobIdList = getInfoList(jobListHtml, jobIdAttr);
			List<String> jobTitleList = getInfoList(jobListHtml, jobTitleAttr);
			List<String> jobDQList = getInfoList(jobListHtml, jobDqAttr);				
			for(int i=0;i<jobIdList.size();i++){
				Map<String, Object> mapObject = new LinkedHashMap<String, Object>();
				mapObject.put("jobId", jobIdList.get(i));
				mapObject.put("jobTitle", jobTitleList.get(i));
				mapObject.put("jobDq", jobDQList.get(i));
				list.add(mapObject);
			}
			rootJsonMap.put("JobList", list);
    	}
    	catch (Exception e) {
			errorMsgList.add(e.toString());
		}

    }
    
    private List<String> getInfoList(String jobListHtml,String attr) {
    	List<String> jobInfoList = new ArrayList<String>();
		int startIndex = -1;
		int endIndex = -1;
		do {
			startIndex = jobListHtml.indexOf(attr);
			if(startIndex != -1){
				endIndex = jobListHtml.indexOf("\"", startIndex+attr.length());
				if(endIndex != -1){
					jobInfoList.add(UnicodeUtil.conver(jobListHtml.substring(startIndex+attr.length(),endIndex)));
					jobListHtml =jobListHtml.substring(endIndex+1);
				}
			}
		} while (startIndex != -1);
		return jobInfoList;
	}
    
	public Map<String, Object> getCandInfoListJsonMap(String website, List<String> htmlContentList) throws DocumentException, IOException{
		
		webSite = website;
		
		if(website.equals(Constant.WEBSITE_LIEPIN)){
			for (String htmlContent:htmlContentList) {
				htmlContent = htmlContent.replaceAll("&nbsp;", "");
				htmlContent = htmlContent.replaceAll("·", "／");
				System.out.println(htmlContent);
				Element rootContentElement = getRootContentElement(website, htmlContent);
				parse(SystemContext.getMsgDoc(website).getRootElement(), rootContentElement);
			}
			//getCandIdList(htmlContent);
		}
		
		rootJsonMap.put(Constant.RTN_MSG_LEVEL_ERROR, errorMsgList);
		rootJsonMap.put(Constant.RTN_MSG_LEVEL_WARN, warnMsgList);
		
		return rootJsonMap;
		/*webSite = website;
		File  file = new File("D:\\2.txt");
		InputStreamReader reader = new InputStreamReader(new FileInputStream(file),"UTF-8");
		BufferedReader bufferedReader = new BufferedReader(reader);
		String str = null;
		String total = "";
		while((str=bufferedReader.readLine())!=null){
			total = total+str;
		}
		total = total.replaceAll("·", "／");
		//System.out.println(total);
		reader.close();
		Document doc = Jsoup.parse(total);
		Element e = doc.select("html").get(0);
		Element body = e.select("body").get(0);
		//9.1.4——简历信息
		//9.1.5——工作经历
		//依次还有项目经历、教育经历、语言能力、自我评价、附加信息
		String location = "9.1";
		String locationArray[] = location.split("\\.");
		Element targetElement = body;
		for (String locStr : locationArray) {
			int loc = Integer.parseInt(locStr);
			if (targetElement.children().size() > loc) {
				targetElement = targetElement.child(loc);
			} else {
				targetElement = null;
				break;
			}
		}
		//System.out.println(targetElement.getElementsByAttributeValue("class", "resume-section section-basic pngfix"));
		//targetElement = targetElement.getElementsByAttributeValue("class", "resume-section section-basic pngfix").first();
		parse(SystemContext.getMsgDoc(website).getRootElement(), targetElement);
		return rootJsonMap;*/
	}
	
	private void parse(org.dom4j.Element rootXMLElement, Element jsoupContentElement) {
		
		if (jsoupContentElement == null) {
			errorMsgList.add("Not found correct html content!");
			return;
		}
		
		if (rootXMLElement == null) {
			errorMsgList.add("Not found correct fetch rule xml content!");
			return;
		}
		List<org.dom4j.Element> xmlElements = rootXMLElement.elements();// xml 配置内容
		
		for (int i = 0; i < xmlElements.size(); i ++) { 
			org.dom4j.Element xmlContentElement =  xmlElements.get(i);
			Attribute nameAttr = xmlContentElement.attribute("attrName");
			Attribute valueAttr = xmlContentElement.attribute("attrValue");
			Attribute contentLocAttr = xmlContentElement.attribute("contentLoc");
			Attribute subIdStartWithAttr = xmlContentElement.attribute("subIdStartWith");
			String contentLoc = (contentLocAttr == null) ? null : contentLocAttr.getValue();
			String subIdStartWith = (subIdStartWithAttr == null) ? null : subIdStartWithAttr.getValue();
			if (null == nameAttr || valueAttr == null) {
				warnMsgList.add("</content> there is no Attributes attrName or attrValue, index=" + i);
				continue;
			}
			String nameAttrV = nameAttr.getValue();
			String valueAttrV = valueAttr.getValue();
			currParseNode = "attrName=["+nameAttrV+"] attrValue=["+valueAttrV+"] subIdStartWith=["+subIdStartWith+"] contentLoc=["+contentLoc+"]";	
			
			// 寻找子内容节点，如id=BPI, EUD,STU 的节点内容, html id的内容与xml配置文件一一对应
			Element contentElement = new JsoupContentFilter(nameAttrV, valueAttrV).accept(jsoupContentElement.children());
			if (contentElement == null) {
				warnMsgList.add("Can not found html content for : " + currParseNode);
				continue;
			} else {
				try {
					putJsonValue(xmlContentElement, contentElement);
				} catch (Exception e) {
					e.printStackTrace();
					warnMsgList.add("Parse " + currParseNode + " error, " + e);
				}
				
			}
		}
		
	}
	
	private void putJsonValue(org.dom4j.Element xmlContentElement, Element contentElement) {
		Attribute contentLocAttr = xmlContentElement.attribute("contentLoc"); // 真正内容节点的父节点位置
		Attribute subIdStartWithAttr = xmlContentElement.attribute("subIdStartWith"); // 分支内容的子ID起始字符
		String contentLoc = (contentLocAttr == null) ? null : contentLocAttr.getValue();
		Element realContentElement = getJsoupElementByLocation(contentElement, contentLoc); // 真正的内容节点，下一层将是实际的内容
		String subIdStartWith = (subIdStartWithAttr == null) ? null : subIdStartWithAttr.getValue();
		Elements subContentElements = new Elements(); // 分个的内容节点
		if (realContentElement == null) {
			//  没有找到真正的内容节点，则直接返回
			warnMsgList.add("Can not found real content for : " + currParseNode);
			return;
		}
		if (StringUtil.isNull(subIdStartWith)) { // 如果 subIdStartWith 属性为空，则
			subContentElements.add(realContentElement);
		} else {
			subContentElements = getMutilSubContentElements(realContentElement, subIdStartWith);
		}
		List<org.dom4j.Element> xmlSubElements = xmlContentElement.elements();
		for (int i = 0; i < subContentElements.size(); i ++) {
			Element subElement = subContentElements.get(i); // 真正包含内容的子节点 即 subIdStartWith 下边的节点
			// loop <msg ...> OR <json ... /><!-- 中文名， ruleClasses 在标签中的值的取值规则 -->
			
			Elements subElements = subElement.children();
			for (int j = 0; j < xmlSubElements.size(); j ++) {
				org.dom4j.Element xmlSubContentElement = xmlSubElements.get(j);	
				String xmlTagName = xmlSubContentElement.getName();
				if (Constant.JSON_TAG_NAME.equals(xmlTagName))  {
					// 处理 content 节点下的json节点
					parseJsonNodeInContent(subElement, xmlSubContentElement);
				}
			}
		}
	}
	
	/*private void putJsonValue(org.dom4j.Element xmlContentElement, Element contentElement) {
		
		Attribute contentLocAttr = xmlContentElement.attribute("contentLoc"); // 真正内容节点的父节点位置
		Attribute subIdStartWithAttr = xmlContentElement.attribute("subIdStartWith"); // 分支内容的子ID起始字符
		Attribute fetchOrderAttr = xmlContentElement.attribute("fetchOrder"); // 是先用哪个过滤条件， 默认为 "contentLoc"
		String contentLoc = (contentLocAttr == null) ? null : contentLocAttr.getValue();
		String subIdStartWith = (subIdStartWithAttr == null) ? null : subIdStartWithAttr.getValue();
		String fetchOrder = (fetchOrderAttr == null) ? "contentLoc" : fetchOrderAttr.getValue();
		
		Elements subContentElements = new Elements(); // 分个的内容节点
		if ("contentLoc".equals(fetchOrder)) {
			Element realContentElement = getJsoupElementByLocation(contentElement, contentLoc); // 真正的内容节点，下一层将是实际的内容
			if (realContentElement == null) {
				//  没有找到真正的内容节点，则直接返回
				warnMsgList.add("Can not found real content for : " + currParseNode);
				return;
			}
			if (StringUtil.isNull(subIdStartWith)) { // 如果 subIdStartWith 属性为空，则
				subContentElements.add(realContentElement);
			} else {
				subContentElements = getMutilSubContentElements(realContentElement, subIdStartWith);
			}
		} else {
			// 另一个值为 "subIdStartWith" , 默认值
			Elements subIdStartWithElements = getMutilSubContentElements(contentElement, subIdStartWith);
			if (null != subIdStartWithElements) {
				for (int i = 0; i < subIdStartWithElements.size(); i ++) {
					Element subContentElement = subIdStartWithElements.get(i);
					if (null != subContentElement) {
						//System.out.println(subContentElement.toString());
						//System.out.println("------------------------------------------------------");
						Element realSubContentElement = getJsoupElementByLocation(subContentElement, contentLoc);
						if (null != realSubContentElement) {
							subContentElements.add(realSubContentElement);
						} else {
							warnMsgList.add("Can not found real content for : " + currParseNode);
						}
					}
				}
			}
		}
		
		if (subContentElements == null) {
			warnMsgList.add("Can not found real content for : " + currParseNode);
			return;
		}
		
		List<org.dom4j.Element> xmlSubElements = xmlContentElement.elements();
		for (int i = 0; i < subContentElements.size(); i ++) {
			Element subElement = subContentElements.get(i); // 真正包含内容的子节点 即 subIdStartWith 下边的节点
			// loop <msg ...> OR <json ... /><!-- 中文名， ruleClasses 在标签中的值的取值规则 -->
			
			Elements subElements = subElement.children();
			for (int j = 0; j < xmlSubElements.size(); j ++) {
				org.dom4j.Element xmlSubContentElement = xmlSubElements.get(j);
				
				String xmlTagName = xmlSubContentElement.getName();
				
				if (Constant.MSG_TAG_NAME.equals(xmlTagName)) {
					// 关键字索引查找内容
					parseMsgNode(subElements, xmlSubContentElement);
				} else if (Constant.SUB_CONTENT_TAG_NAME.equals(xmlTagName)) {
					// 含有子内容的tag， 目前只支持下边是<msg>节点
					Attribute subContentLocAttr = xmlSubContentElement.attribute("subContentloc");
					if (subContentLocAttr == null) {
						// 需要配置 subContentloc
						warnMsgList.add("On [" + currParseNode + "] can not parse </subContent>, there is no Attribute [subContentloc]");
						continue;
					}
					String subContentLoc = subContentLocAttr.getValue();
					Element subSubElement = getJsoupElementByLocation(subElement, subContentLoc); // 包含关键字的节点
					if (subSubElement == null) {
						// 找不到对应位置的内容节点
						warnMsgList.add("On [" + currParseNode + "] can not parse </subContent>, there is no content for its!");
						continue;
					}
					// xmlSubSubContentElements 对应subsubContent 下边的msg
					List<org.dom4j.Element> xmlSubSubContentElements = xmlSubContentElement.elements(); 
					for (int k = 0; k < xmlSubSubContentElements.size(); k ++) {
						org.dom4j.Element xmlSubSubContentElement = xmlSubSubContentElements.get(k);
						parseMsgNode(subSubElement.children(), xmlSubSubContentElement);
					}
					// PARSER SUBCONTENT OVER
				} else if (Constant.JSON_TAG_NAME.equals(xmlTagName))  {
					// 处理 content 节点下的json节点
					parseJsonNodeInContent(subElement, xmlSubContentElement);
				}
			}
		}
	}*/
	
	
	private Elements getMutilSubContentElements(Element contentElement, String subIdStartWithValue) {
		Elements subContentElements = contentElement.children();
		Elements filterElements = new Elements();
		for (int i = 0; i < subContentElements.size(); i ++) {
			Element subContentElement = subContentElements.get(i);
			if (subContentElement.hasAttr("class") && subContentElement.attr("class").startsWith(subIdStartWithValue)) {
				filterElements.add(subContentElement);
			}
		}
		return filterElements;
	}
		
	private void parseJsonNodeInContent(Element subElement,
			org.dom4j.Element xmlSubContentElement) {
		if (subElement == null) {
			return;
		}
		JsonElement jsonElement = JsonElement.parseJsonElement(xmlSubContentElement);
		if (StringUtil.isNull(jsonElement.getJsonName())) {
			return;
		}
		//String jsonValue = getJsoupValueByLocation(subElement, jsonElement.getLocation());
		Element valueElement = getJsoupElementByLocation(subElement, jsonElement.getLocation());
		String jsonValue = null;
		if (null != valueElement) {
			//jsonValue = valueElement.text();
			jsonValue = getJsonValueByRule(valueElement.text(), jsonElement.getRuleClasses());
		} else {
			warnMsgList.add("On [" + currParseNode + "] can not found html content for loc=[" + jsonElement.getLocation() + "], jsonName=[" + jsonElement.getJsonName() + "]");
		}
		addValueToJsonMap(jsonElement, jsonValue);

	}
	
	private String getJsonValueByRule(String nodeValue, String ruleClasses) {
		String jsonValue = nodeValue;
		if (!StringUtil.isNull(ruleClasses) && !StringUtil.isNull(nodeValue)) {
			String ruleClassArray[] = ruleClasses.split("\\,");
			for (String ruleClass : ruleClassArray) {
				//System.out.println(ruleClass);
				String ruleParamArray[] = ruleClass.split("#");
				String ruleParam = (ruleParamArray.length > 1) ? ruleParamArray[1] : null;
				IRule rule = FetchValueRuleUtil.getIRule(ruleParamArray[0]);
				if (rule != null) {
					jsonValue = rule.getValue(jsonValue, ruleParam);
				} else {
					warnMsgList.add("Rule:[" + ruleParamArray + "] not exists.");
				}
			}
		}
		return jsonValue;
	}
	
	private void parseMsgNode(Elements subElements,
			org.dom4j.Element xmlSubContentElement) {
		Attribute kwAttr = xmlSubContentElement.attribute("kw");
		Attribute kwLocAttr = xmlSubContentElement.attribute("kwLoc");
		String kw = kwAttr.getValue();
		String kwLoc = kwLocAttr.getValue();
		if (subElements == null || subElements.size() == 0) {
			warnMsgList.add("On [" + currParseNode + "] can not found html content for kw=["+kw+"], kwLoc=["+kwLoc+"]");
			return;
		}
		// 对应 xmlSubContentElement
		Element subContentElement = new JsoupKeywordFilter(kwLoc, kw).accept(subElements);
		
		// 获取json内容
		List<org.dom4j.Element> xmlJsonElements = xmlSubContentElement.elements();
		for (org.dom4j.Element xmlJsonElement : xmlJsonElements) {
			JsonElement jsonElement = JsonElement.parseJsonElement(xmlJsonElement);
			if (StringUtil.isNull(jsonElement.getJsonName())) {
				continue;
			}
			if (null == subContentElement) {
				warnMsgList.add("On [" + currParseNode + "] can not found kw=["+kw+"], kwLoc=["+kwLoc+"]");
				addValueToJsonMap(jsonElement, null);
				continue;
			}
			// 根据位置找到包含内容的节点，该节点的text就是要找的内容
			Element valueNode = getJsoupElementByLocation(subContentElement, jsonElement.getLocation());

			if (valueNode == null) {
				warnMsgList.add("On [" + currParseNode + "]  kw=["+kw+"], kwLoc=["+kwLoc+"], can not found html content for loc=[" + jsonElement.getLocation() + "]");
				addValueToJsonMap(jsonElement, null);
			} else {
				String nodeValue = valueNode.text();
				String jsonValue = getJsonValueByRule(nodeValue, jsonElement.getRuleClasses());
				// 将解析出来的值放入jsonmap
				addValueToJsonMap(jsonElement, jsonValue);
			}
		}
	}
	
private void addValueToJsonMap(JsonElement jsonElement, Object jsonValue) {
		
		String mapNames = jsonElement.getMapName();
		String jsonName = jsonElement.getJsonName();
		boolean isCreateNewListObject = jsonElement.isCreateNewRootMap();
		boolean isEnum = jsonElement.isEnum();
		String dataType = jsonElement.getDataType();
		String fid = jsonElement.getFid();
		boolean isSplit = jsonElement.isSplit();
		
		if (jsonValue == null) {
			//jsonValue = "";
			return;
		}
		if (StringUtil.isNull(jsonName)) {
			// 如果不配置jsonName，则不能进入jsonMap
			return;
		}
		
		// 用 | 将内容分割成两个内容
		if (isSplit) {
			splitFetchValue(jsonElement, jsonValue);
			return;
		}
		
		// 根据函数api获得数据
		if (!StringUtil.isNull(fid)) {
			addFunctionResultToMap(jsonElement, jsonValue);
			return;
		}
		
		// 获取枚举值
		if (isEnum) {
			String jsonMapAndName = null;
			if (StringUtil.isNull(mapNames)) {
				jsonMapAndName = jsonName;
			} else {
				jsonMapAndName = mapNames + "#" + jsonName;
			}
			jsonValue = FetchValueRuleUtil.getEnumValue(webSite,jsonMapAndName, (String)jsonValue, isEnum);
//			if (Constant.ENUM_UN_KNOWN.equals(jsonValue)) {
//				warnMsgList.add("On [" + currParseNode + "] [" + jsonMapAndName + "] can not found match enum value, use unknown!");
//			}
		}
		
		Object jValue = null;
		// 数据类型
		if (!StringUtil.isNull(dataType) && !Constant.DATA_TYPE_STRING.equals(dataType)) {
			jValue  = DataTypeUtil.getValue(jsonValue.toString(), dataType);
			if (jValue == null) {
				warnMsgList.add("On [" + currParseNode + "] " + StringUtil.getString(mapNames) 
						+ "#" + jsonName + " can not convert [" + jsonValue  + "] to DataType:[" + dataType + "]");
			}
		} else {
			jValue = jsonValue;
		}
		
		// 字符串转码
		if (jValue instanceof String) {
			jValue = UnicodeUtil.conver((String)jValue);
		}
		
		if (StringUtil.isNull(mapNames)) {
			// 如果mapName是空，则直接放到根目录下
			rootJsonMap.put(jsonName, jValue);
			return;
		}
		
		String mapNameArray[] = mapNames.split("#");
		
		if (jsonElement.isOnlyMap()) {
			Map<String, Object> currMap = (Map<String, Object>) rootJsonMap.get(mapNameArray[0]); 
			if (currMap == null) {
				currMap = new LinkedHashMap<String, Object>();
				rootJsonMap.put(mapNameArray[0], currMap);
			}
			Map<String, Object> subMap = null;
			if (mapNameArray.length == 1) {
				subMap = currMap;
			} else {
				for (int i = 1; i < mapNameArray.length; i ++) {
					subMap = (Map<String, Object>) currMap.get(mapNameArray[i]);
					if (null == subMap) {
						subMap = new LinkedHashMap<String, Object>();
						currMap.put(mapNameArray[i], subMap);
					}
					currMap = subMap;
				}
			}
			subMap.put(jsonName, jValue);
			return;
		}
		
		List<Map<String, Object>> mapNameMapList = (List<Map<String, Object>>) rootJsonMap.get(mapNameArray[0]);
		if (mapNameMapList == null) {
			mapNameMapList = new ArrayList<Map<String, Object>>();
			rootJsonMap.put(mapNameArray[0], mapNameMapList);
		}
		
		Map<String, Object> jsonMapInList = null;
		if (isCreateNewListObject || mapNameMapList.size() == 0) {
			jsonMapInList = new LinkedHashMap<String, Object>();
			mapNameMapList.add(jsonMapInList);
		} else {
			jsonMapInList = mapNameMapList.get(mapNameMapList.size() - 1);
		}
		if (mapNameArray.length == 1) {
			jsonMapInList.put(jsonName, jValue);
		} else {
			Map<String, Object> currMap = jsonMapInList;
			for (int i = 1; i < mapNameArray.length; i ++) {
				String subMapName = mapNameArray[i];
				Map<String, Object> subMap = (Map<String, Object>) currMap.get(subMapName);
				if (subMap == null) {
					subMap = new LinkedHashMap<String, Object>();
					currMap.put(subMapName, subMap);
				}
				if (i == mapNameArray.length - 1) {
					subMap.put(jsonName, jValue);
				} else {
					currMap = subMap;
				}
			}
		}	
	}
	
	private Element getRootContentElement(String website, String htmlContent) {
		Document doc = Jsoup.parse(htmlContent);
		Element e = doc.select("html").get(0);
		Element body = e.select("body").get(0);
		Properties configProperties = null;
		try {
			configProperties = SystemContext.getConfigProperties(website);
		} catch (IOException e1) {
			errorMsgList.add("Parse config file error:" + e1);
			e1.printStackTrace();
		} catch (DocumentException e1) {
			errorMsgList.add("Parse config file error:" + e1);
			e1.printStackTrace();
		}
		if (configProperties == null) {
			return null;
		}
		// 真正内容相对于body节点的位置
		String contentLocation = configProperties.getProperty(Constant.CONTENT_LOCATION);
		Element rcontent = getJsoupElementByLocation(body, contentLocation);
		
		return rcontent;
	}
	
	private Element getJsoupElementByLocation(final Element contentElement, String location) {
		if (StringUtil.isNull(location)) {
			return contentElement;
		} else {
			Element targetElement = contentElement;
			String locationArray[] = location.split("\\.");
			for (String locStr : locationArray) {
				int loc = Integer.parseInt(locStr);
				if (targetElement.children().size() > loc) {
					targetElement = targetElement.child(loc);
				} else {
					targetElement = null;
					break;
				}
			}
			return targetElement;
		}
	}
	
	private void splitFetchValue(JsonElement jsonElement, Object jsonValue) {
		if (jsonValue != null) {
			String oValue = jsonValue.toString();
			//String vArray[] = oValue.split("\\|");
			String vArray[] = oValue.split("、");
			JsonElement copyElement = jsonElement.clone();
			copyElement.setSplit(false);
			for (String vValue : vArray) {
				addValueToJsonMap(copyElement, vValue);
			}
		}
	}
	
	private void addFunctionResultToMap(JsonElement jsonElement, Object jsonValue) {
		if (null != jsonValue) {
			Map<String, Object> qryResult = FunctionUtil.getKeyValueListByFunction(jsonElement.getFid(), jsonElement.getJsonName(), jsonValue.toString());
			if (qryResult.remove(Constant.RTN_MSG_LEVEL_ERROR) != null) {
				addValueToJsonMap(jsonElement, jsonValue);
			} else {
				int index = 0;
				JsonElement copyElement = jsonElement.clone();
				copyElement.setDataType(null);
				copyElement.setEnum(false);
				copyElement.setFid(null);
				for (Iterator<String> iter = qryResult.keySet().iterator(); iter.hasNext();) {
					String qryName = iter.next();
					Object qryValue = qryResult.get(qryName);
					if (index > 0){
						copyElement.setCreateNewRootMap(false);
					}
					copyElement.setJsonName(qryName);
					addValueToJsonMap(copyElement, qryValue);
					index += 1;
				}
			}
		}
	}
}
