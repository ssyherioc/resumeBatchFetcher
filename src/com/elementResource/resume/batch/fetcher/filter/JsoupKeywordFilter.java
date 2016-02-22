package com.elementResource.resume.batch.fetcher.filter;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupKeywordFilter implements JsoupFilter {

	private String kw;
	
	private String kwLocation;
	
	public JsoupKeywordFilter(String kwLocation, String kw) {
		this.kw = kw;
		this.kwLocation = kwLocation;
	}
	
	@Override
	public Element accept(Elements elements) {
		for (int i = 0; i < elements.size(); i ++) {
			Element element = elements.get(i);
			Elements subElements = element.children(); // loop  <dl class="lineDl lineDl_text"> 
			if (null != subElements && subElements.size() > Integer.parseInt(kwLocation) + 1) {
				Element kwEle = subElements.get(Integer.parseInt(kwLocation));
				String thisEleKw = kwEle.text();
				//if (kw.equals(thisEleKw)) {
				if (thisEleKw.contains(kw)) {
					return element;
				}
			}
		}
		return null;
	}
	
	
	
}
