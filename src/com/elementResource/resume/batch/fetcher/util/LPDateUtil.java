/**
 * 
 */
package com.elementResource.resume.batch.fetcher.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.elementResource.resume.batch.fetcher.context.SystemContext;

/**
 * @author qianeryu
 *
 */
public class LPDateUtil {
	
	public static SimpleDateFormat outputSf = new SimpleDateFormat( "yyyy-MM-dd");
	public static SimpleDateFormat inputSf = null;
	public static String TODAY_STRING = "999999";
	public static String SPLIT_CHAR = null;
	
	/*static {
		try {
			inputSf = new SimpleDateFormat(SystemContext.getConfigProperties(Constant.WEBSITE_LIEPIN).getProperty("input_date_format"));
		} catch (Exception e) {
			e.printStackTrace();
			inputSf = new SimpleDateFormat("yyyyMM");
		}
	}*/
	
	public static String parse(String dateStr, int index) {
		if(index == 0){
			try {
				inputSf = new SimpleDateFormat(SystemContext.getConfigProperties(Constant.WEBSITE_LIEPIN).getProperty("input_date_format"));
			} catch (Exception e) {
				e.printStackTrace();
				inputSf = new SimpleDateFormat("yyyyMM");
			}
		}
		else if(index == 1){
			try {
				inputSf = new SimpleDateFormat(SystemContext.getConfigProperties(Constant.WEBSITE_LIEPIN).getProperty("input_date_format"));
			} catch (Exception e) {
				e.printStackTrace();
				inputSf = new SimpleDateFormat("yyyyMM");
			}
			if(TODAY_STRING.equals(dateStr)){
				return Constant.DATE_TODAY_RTN_VALUE;
			}
		}
		else if(index == 2){
			try {
				inputSf = new SimpleDateFormat(SystemContext.getConfigProperties(Constant.WEBSITE_LIEPIN).getProperty("input_birthdate_format"));
			} catch (Exception e) {
				e.printStackTrace();
				inputSf = new SimpleDateFormat("yyyy");
			}
		}
		try{
			Date d = inputSf.parse(dateStr);
			return getDateStr(d);
		}catch(ParseException e){
			return dateStr;
		}
	}
	
	public static String getDateStr(Date d, String format) {
		SimpleDateFormat sf = new SimpleDateFormat(format);
		return sf.format(d);
	}
	
	public static String getDateStr(Date d) {
		return outputSf.format(d);
	}
	
	public static void main(String args[]) {
		System.out.println(parse("200907", 2));
	}
	
}
