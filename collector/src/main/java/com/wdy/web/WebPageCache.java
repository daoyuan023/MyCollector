package com.wdy.web;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebPageCache {
	private static final Logger logger = LoggerFactory.getLogger(WebPageCache.class);

	private static Map<String, WebPage> mapWebPageCache = new HashMap<String, WebPage>();

	public static WebPage getWebPage(String url) {
		WebPage page = null;

		if (!mapWebPageCache.containsKey(url)) {
			logger.debug("Requested page not found in cache, download it: {}", url);
			page = new WebPage(url);
			if (page.downloadPageContent()) {
				mapWebPageCache.put(url, page);
			} else {
				logger.warn("Page download failed, please ensure network is available and URL is valid: {}", url);
				page = null;
			}
		} else {
			page = mapWebPageCache.get(url);
			page.refresh();
		}
		return page;
	}

	public static void refresh() {
		for (WebPage page : mapWebPageCache.values()) {
			page.refresh();
		}
	}

	public static boolean contains(String url) {
		return mapWebPageCache.containsKey(url);
	}
}
