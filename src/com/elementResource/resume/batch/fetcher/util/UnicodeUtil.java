package com.elementResource.resume.batch.fetcher.util;

public class UnicodeUtil {

	public static void main(String args[]) {

		String cc = "钱二余123";

		String dd = UnicodeUtil.conver(cc);
		System.out.println(dd);
		System.out.println(UnicodeUtil.conver("123"));
		
		System.out.println(UnicodeUtil.reconvert("\u738b\u540e\u6839123"));
	}

	public static String reconvert(String str) {
		char[] c = str.toCharArray();
		String resultStr = "";
		for (int i = 0; i < c.length; i++)
			resultStr += String.valueOf(c[i]);
		return resultStr;
	}

	public static String conver(String str) {
		String result = "";
		for (int i = 0; i < str.length(); i++) {
			String temp = "";
			int strInt = str.charAt(i);
			if (strInt > 127) {
				temp += "\\u" + Integer.toHexString(strInt);
			} else {
				temp = String.valueOf(str.charAt(i));
			}
			result += temp;
		}
		return result;
	}

}
