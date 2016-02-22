package com.elementResource.resume.batch.fetcher.fetchValueRule;

/**
 * [0]:原始值
 * [1]:规则，XX@YY  XX=分隔符，YY=位置  
 * @author qianeryu
 *
 */
public class BracketSplitRule implements IRule{

	@Override
	public String getValue(String... value) {
		String rtnValue = null;
		if (value != null && value.length >= 2) {
			rtnValue = value[0];
			if (value[0] != null && value[1] != null) {
				try {
					String array[] = rtnValue.split("\\[");
					if (array.length > Integer.parseInt(value[1])) {
							rtnValue = array[Integer.parseInt(value[1])];
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return rtnValue;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	

}
