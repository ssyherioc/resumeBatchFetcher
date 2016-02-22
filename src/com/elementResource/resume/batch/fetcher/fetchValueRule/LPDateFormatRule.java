package com.elementResource.resume.batch.fetcher.fetchValueRule;

import com.elementResource.resume.batch.fetcher.util.LPDateUtil;


/**
 * [0]原始值
 * [1]0=开始日期；1=结束日期,2=出生年月
 * @author qianeryu
 *
 */
public class LPDateFormatRule implements IRule {

	@Override
	public String getValue(String... value) {
		if (value.length >= 2) {
			return LPDateUtil.parse(value[0], Integer.parseInt(value[1]));
		} else {
			return value[0];
		}
	}

}
