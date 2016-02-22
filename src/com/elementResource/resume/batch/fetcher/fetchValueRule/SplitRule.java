package com.elementResource.resume.batch.fetcher.fetchValueRule;

/**
 * [0]:原始值
 * [1]:规则，XX@YY  XX=分隔符，YY=位置  
 * @author qianeryu
 *
 */
public class SplitRule implements IRule{

	@Override
	public String getValue(String... value) {
		String rtnValue = null;
		if (value != null && value.length >= 2) {
			rtnValue = value[0];
			if (value[0] != null && value[1] != null) {
				try {
					String array[] = rtnValue.split("\\|");
					if (array.length > Integer.parseInt(value[1])) {
							rtnValue = array[Integer.parseInt(value[1])];
							rtnValue = rtnValue.substring(rtnValue.indexOf("：")+1).trim();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return rtnValue;
	}
	
	public static void main(String args[]) {
		SplitRule r = new SplitRule();
		System.out.println(r.getValue("公司性质：外商独资·外企办事处 |公司规模：10000人以上 |公司行业：保险","2"));
	}
}
