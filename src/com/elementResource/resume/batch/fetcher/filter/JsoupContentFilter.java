/**
 * 
 */
package com.elementResource.resume.batch.fetcher.filter;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author qianeryu
 *
 */
public class JsoupContentFilter implements JsoupFilter {

	private String attrName;
	private String attrValue;
	
	public JsoupContentFilter(String attrName, String attrValue) {
		this.attrName = attrName;
		this.attrValue = attrValue;
	}
	
	
	@Override
	public Element accept(Elements elements) {
		if (null != elements) {
			for (int i = elements.size()-1; i >= 0; i --) {
				Element element = elements.get(i);
				if (attrValue.equals(element.attr(attrName))) {
					return element;
				}
			}
		}
		return null;
	}

}
