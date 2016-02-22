package com.elementResource.resume.batch.fetcher.fetchValueRule;

import java.io.UnsupportedEncodingException;

/**
 * 从方括号中取内容，分隔符为、
 * @author qianeryu
 *
 */
public class BracketFetchRule implements IRule {

	@Override
	public String getValue(String... value) {
		String rtnValue = null;
		if (null != value && null != value[0] && null != value[1]) {
			int index1 = value[0].indexOf("[");
			int index2 = value[0].indexOf("]");
			if (index1 != -1 && index2 != -1) {
				String compositeValue = value[0].substring(index1 + 1, index2);
				try {
					String array[] = compositeValue.split("、");
					if (array.length > Integer.parseInt(value[1])) {
						rtnValue = array[Integer.parseInt(value[1])].trim();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return rtnValue;
	}

}
