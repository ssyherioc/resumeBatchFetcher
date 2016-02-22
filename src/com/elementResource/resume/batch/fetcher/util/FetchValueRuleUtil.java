/**
 * 
 */
package com.elementResource.resume.batch.fetcher.util;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.DocumentException;

import com.elementResource.resume.batch.fetcher.context.SystemContext;
import com.elementResource.resume.batch.fetcher.fetchValueRule.IRule;


/**
 * 将html节点中值映射为json只的规则
 * @author qianeryu
 *
 */
public class FetchValueRuleUtil {

	/** 取节点中值的定义 */
	private static Map<String, IRule> FETCH_RULE_MAP = new HashMap<String, IRule>();
	
	private static String RULE_CLASS_PACKAGE = IRule.class.getPackage().getName();
	
	public static IRule getIRule(String ruleClass) {
		IRule rule = null;
		if (!StringUtil.isNull(ruleClass)) {
			if (FETCH_RULE_MAP.containsKey(ruleClass)) {
				rule = FETCH_RULE_MAP.get(ruleClass);
			} else {
				String className = RULE_CLASS_PACKAGE + "." + ruleClass;
				try {
					Class clazz = Class.forName(className);	
					Constructor ctr = clazz.getConstructor(null);
					Object obj = ctr.newInstance(null);
					rule = (IRule) obj;
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					// 有可能会找不到规则类
				} catch (NoSuchMethodException e) {
				} catch (IllegalAccessException e) {
				} catch (InvocationTargetException e){
				} catch (InstantiationException e) {
				}
			}
		}
		return rule;
	}
	
	public static String getEnumValue(String jsonMapAndName, String jsonValue, boolean enumBoolean) {
		return getEnumValue(Constant.WEBSITE_51JOB, jsonMapAndName, jsonValue, enumBoolean);
	}
	
	public static String getEnumValue(String website, String jsonMapAndName, String jsonValue, boolean enumBoolean) {
		if (enumBoolean) {
			try {
				Map<String, Map<String, String>> enumMap = SystemContext.getEnumMap(website);
				if (null != enumMap) {
					Map<String, String> enumSubMap = enumMap.get(jsonMapAndName);
					if (null != enumSubMap) {
						String enumValue = enumSubMap.get(jsonValue);
						if (StringUtil.isNull(enumValue)) {
							//return Constant.ENUM_UN_KNOWN;
							enumValue = enumSubMap.get(Constant.DEFAULT_ENUM);
							if (enumValue == null) {
								return Constant.ENUM_UN_KNOWN; 
							} else {
								return enumValue;
							}
						} else {
							return enumValue;
						}
					}
				}
			} catch (DocumentException e) {
				e.printStackTrace();
				return Constant.ENUM_UN_KNOWN;
			} catch (IOException e) {
				e.printStackTrace();
				return Constant.ENUM_UN_KNOWN;
			}
			return Constant.ENUM_UN_KNOWN;
		} else {
			return jsonValue;
		}
	}
	
	public static void main(String args[]) {
		String rc = "TextRule";
		IRule rule = getIRule(rc);
		System.out.println(rule.getValue("AAAA&nbsp;"));
	}
}
