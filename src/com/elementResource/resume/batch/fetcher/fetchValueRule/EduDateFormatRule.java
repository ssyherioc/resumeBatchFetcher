package com.elementResource.resume.batch.fetcher.fetchValueRule;

import com.elementResource.resume.batch.fetcher.util.LPDateUtil;



/**
 * [0]原始值
 * [1]0=开始日期；1=结束日期
 * @author qianeryu
 *
 */
public class EduDateFormatRule implements IRule {

	@Override
	public String getValue(String... value) {
		if (value.length >= 2) {
			String date = value[0].split(" ")[1];
			if(value[1].equals("0")){
				date = date.substring(0,date.indexOf("–"));
			}
			else if(value[1].equals("1")){
				date = date.substring(date.indexOf("–")+1);
			}
			return LPDateUtil.parse(date, Integer.parseInt(value[1]));
		} else {
			return value[0];
		}
	}
	
	public static void main(String args[]) {
		EduDateFormatRule r = new EduDateFormatRule();
		System.out.println(r.getValue("圣路易斯华盛顿大学 2012/08–2015/05","1"));
	}

}
