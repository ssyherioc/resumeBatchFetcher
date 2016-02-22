/**
 * 
 */
package com.elementResource.resume.batch.fetcher.fetchValueRule;

import java.math.BigDecimal;

/**
 * @author qianeryu
 *
 */
public class ExpectSalaryRule implements IRule {

	/* (non-Javadoc)
	 * @see com.elementResource.resume.fetcher.fetchValueRule.IRule#getValue(java.lang.String[])
	 */
	@Override
	public String getValue(String... value) {
		String rtnValue = value[0];
		if(!rtnValue.equals("面议")){
			rtnValue = rtnValue.substring(0,rtnValue.indexOf("（"));
			if(rtnValue.indexOf("万") != -1){
				rtnValue = rtnValue.replace("万", "");
				double valueD = Double.parseDouble(rtnValue);
				valueD = valueD * 10000;
				int valueI = (int) valueD;
				return valueI+"";
			 }
		}
		return "";
	}
	

	
	public static void main(String args[]) {
		ExpectSalaryRule r = new ExpectSalaryRule();
		System.out.println(r.getValue("面议"));
		System.out.println(r.getValue("10.79万（8300元/月 * 13个月）"));
		
	}

}
