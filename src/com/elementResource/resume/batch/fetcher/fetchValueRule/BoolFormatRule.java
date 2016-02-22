package com.elementResource.resume.batch.fetcher.fetchValueRule;

import com.elementResource.resume.batch.fetcher.util.BoolUtil;


/**
 * [0]原始值
 * 
 * @author qianeryu
 *
 */
public class BoolFormatRule implements IRule {

	@Override
	public String getValue(String... value) {
		return BoolUtil.parse(value[0]);
	}

}
