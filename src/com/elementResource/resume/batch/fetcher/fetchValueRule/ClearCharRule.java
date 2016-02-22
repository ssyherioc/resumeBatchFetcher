/**
 * 
 */
package com.elementResource.resume.batch.fetcher.fetchValueRule;

import java.util.ArrayList;
import java.util.List;




/**
 * @author EllaYang
 *
 */
public class ClearCharRule implements IRule {

	/* (non-Javadoc)
	 * @see com.elementResource.resume.fetcher.fetchValueRule.IRule#getValue(java.lang.String[])
	 */
	@Override
	public String getValue(String... value) {
		
		String v = value[0];
		if (value.length >=2 && value[1] != null) {
			String bValue = value[1].split(";")[0];
			String aValue = value[1].split(";")[1];
			List<Integer> bIndex =  new ArrayList<Integer>();
			List<Integer> aIndex =  new ArrayList<Integer>();
			int index = -1;
			String tmp = v;
			while((index = tmp.indexOf(bValue,index+1))!= -1){
				bIndex.add(index);
				//tmp = tmp.substring(index+1);
			}
			index= -1;
			tmp = v;
			while((index = tmp.indexOf(aValue,index+1))!= -1){
				aIndex.add(index);
				//tmp = tmp.substring(index+1);
			}
			for (int i = bIndex.size() -1; i >=0; i--) {
				v = v.substring(0,bIndex.get(i))+v.substring(aIndex.get(i)+1);
			}
		}
		return v;
	}
	
	public static void main(String args[]) {
		ClearCharRule r = new ClearCharRule();
		System.out.println(r.getValue("英语(CET6、同声翻译)、法语(读写精通)、普通话(一级甲等)","(",")"));
	}
}
