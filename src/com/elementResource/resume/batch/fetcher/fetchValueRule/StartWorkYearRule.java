/**
 * 
 */
package com.elementResource.resume.batch.fetcher.fetchValueRule;

import java.util.Calendar;

/**
 * @author qianeryu
 *
 */
public class StartWorkYearRule implements IRule {

	/* (non-Javadoc)
	 * @see com.elementResource.resume.fetcher.fetchValueRule.IRule#getValue(java.lang.String[])
	 */
	@Override
	public String getValue(String... value) {
		if (null != value[0]) {
			int y = 0;
			if (value[0].indexOf("以上") != -1) {
				y = Integer.parseInt(value[0].replace("以上", ""));
			} else if (value[0].indexOf("-") != -1){
				String yearArray[] = value[0].split("-");
				int y1 = 0;
				int y2 = 0;
				try{
					y1 = Integer.parseInt(yearArray[0]);
				} catch (Exception e){}
				try {
					y2 = Integer.parseInt(yearArray[1]);
				} catch (Exception e){}
				y = (y1 + y2) / 2;
			}else{
				y=Integer.parseInt(value[0]);
			}
			int currYear = Calendar.getInstance().get(Calendar.YEAR);
			return (currYear - y) + "";
		}
		return null;
	}

	
	public static void main(String args[]) {
		StartWorkYearRule r = new StartWorkYearRule();
		System.out.println(r.getValue("5-8"));
	}
}
