package com.elementResource.resume.batch.fetcher.filter;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public interface JsoupFilter {

	public Element accept(Elements elements);
	
}
