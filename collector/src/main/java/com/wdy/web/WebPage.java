package com.wdy.web;

import java.io.IOException;
import java.time.LocalDateTime;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wdy.common.Utils;

public class WebPage {
	private static final Logger logger = LoggerFactory.getLogger(WebPage.class);

	private static final int REFRESH_INTERVAL = 60; // seconds
	private String htmlPageContent;
	private String url;
	private LocalDateTime lastRefreshTime;

	public WebPage(String url) {
		this.url = url;
	}

	public boolean downloadPageContent() {
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		request.addHeader("User-Agent", "Apache HTTPClient");
		HttpResponse response;
		try {
			response = client.execute(request);
			HttpEntity entity = response.getEntity();
			htmlPageContent = EntityUtils.toString(entity);
			logger.debug("Successfully download html page: {}", url);
			// logger.debug("Html page content:\n{}", htmlPage);
		} catch (ParseException | IOException e) {
			logger.error("Failed to download html page '{}': {}", url, e.getMessage());
			e.printStackTrace();
			return false;
		}

		lastRefreshTime = LocalDateTime.now();
		return true;
	}

	public void refresh() {
		refresh(false);
	}
	
	public void refresh(boolean force) {
		if (force || isDated()) {
			downloadPageContent();
		}
	}
	
	private boolean isDated() {
		return lastRefreshTime.isBefore(LocalDateTime.now().minusSeconds(REFRESH_INTERVAL));
	}

	public String getHtmlPageContent() {
		return htmlPageContent;
	}

	public String getFieldAsString(String pattern, int matchIndex) {
		String fieldValue = Utils.extractInfo(this.htmlPageContent, pattern, matchIndex).trim();
		if (fieldValue != null && !fieldValue.isEmpty()) {
			logger.debug("Found target field with value: '{}'.", fieldValue);
		} else {
			logger.info("Not found target field, please ensure page field is configured correctly.");
		}
		return fieldValue;
	}

	public int getFieldAsInt(String pattern, int matchIndex) {
		String fieldValue = getFieldAsString(pattern, matchIndex);
		if (fieldValue != null && !fieldValue.isEmpty()) {
			try {
				return Integer.parseInt(fieldValue);
			} catch (NumberFormatException e) {
				logger.error("Unexpected number format: {}", fieldValue);
			}
		}
		return 0;
	}
}
