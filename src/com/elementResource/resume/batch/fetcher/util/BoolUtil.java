/**
 * 
 */
package com.elementResource.resume.batch.fetcher.util;

/**
 * @author qianeryu
 *
 */
public class BoolUtil {
		
	public static String parse(String boolstr) {
		if(boolstr.equals("0")){
			return "false";
		}
		else{
			return "true";
		}
	}
		
}
