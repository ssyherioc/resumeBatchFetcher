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
public class DateUtil {
	
	public static SimpleDateFormat outputSf = new SimpleDateFormat( "yyyy-MM-dd");
	public static SimpleDateFormat inputSf = null;
	public static String TODAY_STRING = "至今";
	public static String SPLIT_CHAR = null;
	
	static {
		try {
			inputSf = new SimpleDateFormat(SystemContext.getConfigProperties(Constant.WEBSITE_51JOB).getProperty("input_date_format"));
			SPLIT_CHAR = SystemContext.getConfigProperties(Constant.WEBSITE_51JOB).getProperty("input_date_split_char");
		} catch (Exception e) {
			e.printStackTrace();
			inputSf = new SimpleDateFormat("yyyy/M");
		}
	}
	
	public static String parse(String dateStr, int index) {
		String array[] = dateStr.split(SPLIT_CHAR);
		if (array.length > index) {
			String target = array[index];
			if (TODAY_STRING.equals(target)) {
				return Constant.DATE_TODAY_RTN_VALUE;
			}
			try {
				Date d = inputSf.parse(target);
				return getDateStr(d);
			} catch (ParseException e) {
				return dateStr;
			}
		} else {
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
		System.out.println(parse("2013/12-至今", 1));
	}
	
}
