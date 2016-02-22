/**
 * 
 */
package com.elementResource.resume.batch.fetcher.fetchValueRule;




/**
 * @author EllaYang
 *
 */
public class ReplaceCharRule implements IRule {

	/* (non-Javadoc)
	 * @see com.elementResource.resume.fetcher.fetchValueRule.IRule#getValue(java.lang.String[])
	 */
	@Override
	public String getValue(String... value) {
		
		String v = value[0];
		if (value.length >=2 && value[1] != null) {
			v = v.replaceAll(value[1], "").trim();
		}
		return v;
	}

}
