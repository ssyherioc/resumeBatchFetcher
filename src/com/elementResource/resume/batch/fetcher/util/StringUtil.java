package com.elementResource.resume.batch.fetcher.util;

public class StringUtil {
	
	public static boolean isNull(String str) {
		if (null == str || "".equals(str.trim())) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String getString(String str) {
		if (str == null) {
			return "";
		} else {
			return str;
		}
	}
}
