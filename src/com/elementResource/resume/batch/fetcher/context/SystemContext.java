package com.elementResource.resume.batch.fetcher.context;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.elementResource.resume.batch.fetcher.util.Constant;

public class SystemContext {

	private static Map<String, Properties> CONFIG_PROPERTIES = new HashMap<String, Properties>();
	
	private static List<String> CONFIG_WEBSITES = new ArrayList<String>();
	
	private static Map<String, Document> MSG_FETCH_RULE_DOC = new HashMap<String, Document>();
	
	private static Map<String, Map<String, Map<String, String>>> ENUM_MAP = new HashMap<String, Map<String,Map<String,String>>>();
	
	/** API配置 */
	private static final String FUNCTION_CONFIG = "function.xml";
	
	private static Map<String, String> FUNCTION_JSON = new HashMap<String, String>();
	
	private static String FUNCTION_URL = "";
	
	public static Properties getConfigProperties(String website) throws DocumentException, IOException {
		init(website);
		return CONFIG_PROPERTIES.get(website);
	}
	
	private static void init(String website) throws DocumentException, IOException {
		if (CONFIG_WEBSITES.contains(website) == false) {
			String rootPath = getRootPath();
			
			File msgFile = new File(rootPath + website + "." + Constant.MSG_FETCH_RULE_PATH);
			SAXReader reader = new SAXReader();
			Document msgDoc = reader.read(msgFile);
			MSG_FETCH_RULE_DOC.put(website, msgDoc);
			
			InputStream configIS = new FileInputStream(rootPath + website + "." + Constant.PROPERTIES);
			Properties prop = new Properties();
			prop.load(configIS);
			CONFIG_PROPERTIES.put(website, prop);
			
			File enumFile = new File(rootPath + website + "." + Constant.ENMU_RULE_PATH);
			SAXReader reader2 = new SAXReader();
			Document enumDoc = reader2.read(enumFile);
			initEnum(website, enumDoc);
			
			CONFIG_WEBSITES.add(website);
		}
	}
	
	public static String getRootPath(){  
		String filePath = System.getProperty("user.dir");
        String pathSplit = System.getProperty("path.separator");//windows����";",linux����":"  
        if(filePath.contains(pathSplit)){  
            filePath = filePath.substring(0,filePath.indexOf(pathSplit));  
        }else if (filePath.endsWith(".jar")) {//��ȡ·���е�jar����,��ִ��jar�����еĽ�����".jar"  
            filePath = filePath.substring(0, filePath.lastIndexOf(File.separator) + 1);  
        }      
        return filePath + "/config/";
    }  
	
	private static void initEnum(String website, Document enumDoc) {
		Map<String, Map<String, String>> websiteEnumMap = ENUM_MAP.get(website);
		if (null == websiteEnumMap) {
			websiteEnumMap = new HashMap<String, Map<String,String>>();
			ENUM_MAP.put(website, websiteEnumMap);
		} else {
			websiteEnumMap.clear();
		}
		
		Element rootElement = enumDoc.getRootElement();
		List enumElementList = rootElement.elements();
		for (int i = 0; i< enumElementList.size(); i ++) {
			Element enumEle = (Element) enumElementList.get(i);
			Attribute nameAttr = enumEle.attribute("name");
			Attribute defaultAttr = enumEle.attribute("default");
			
			Map<String, String> enumMap = new HashMap<String, String>();
			websiteEnumMap.put(nameAttr.getValue(), enumMap);
			
			enumMap.put(Constant.DEFAULT_ENUM, defaultAttr.getValue());
			
			List enumSubEles = enumEle.elements();
			for (int j = 0; j < enumSubEles.size(); j ++) {
				Element enumSubEle = (Element) enumSubEles.get(j);
				Attribute displayAttr = enumSubEle.attribute("display");
				Attribute codeAttr = enumSubEle.attribute("code");
				enumMap.put(displayAttr.getValue(), codeAttr.getValue());
			}
		}
	}
	
	public static Document getMsgDoc(String website) throws DocumentException, IOException {
		init(website);
		return MSG_FETCH_RULE_DOC.get(website);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public static Map<String, Map<String, String>> getEnumMap(String website) throws DocumentException, IOException {
		init(website);
		return ENUM_MAP.get(website);
	}
	public static String getFunctionEntity(String functionId) {
		initFunction();
		return FUNCTION_JSON.get(functionId);
	}
	
	public static String getFunctionURL() {
		initFunction();
		return FUNCTION_URL;
	}
	private static void initFunction() {
		if (FUNCTION_JSON.isEmpty()) {
			String rootPath = getRootPath();
			File functionFile = new File(rootPath + FUNCTION_CONFIG);
			SAXReader reader3 = new SAXReader();
			Document functionDoc = null;
			try {
				functionDoc = reader3.read(functionFile);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
			Element rootElement = functionDoc.getRootElement();
			FUNCTION_URL = rootElement.elementText("functionURL");
			
			List fidElementList = rootElement.elements("function");
			for (int i = 0; i < fidElementList.size(); i ++) {
				Element functionElement = (Element) fidElementList.get(i);
				Attribute fidNameAttr = functionElement.attribute("name");
				String fid = fidNameAttr.getValue();
				String jsonValue = functionElement.getText();
				FUNCTION_JSON.put(fid, jsonValue);
			}
		}
	}

}
