package com.wdy.crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wdy.common.Config;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {
	private static final Logger logger = LoggerFactory.getLogger(MyCrawler.class);
	private static final Pattern IMAGE_EXTENSIONS = Pattern.compile(".*\\.(bmp|gif|jpg|png)$");

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		// Ignore the url if it has an extension that matches our defined set of
		// image extensions.
		if (IMAGE_EXTENSIONS.matcher(href).matches()) {
			return false;
		}
		return true;
	}

	@Override
	public void visit(Page page) {
		int docid = page.getWebURL().getDocid();
		String url = page.getWebURL().getURL();
		String domain = page.getWebURL().getDomain();
		String path = page.getWebURL().getPath();
		String subDomain = page.getWebURL().getSubDomain();
		String parentUrl = page.getWebURL().getParentUrl();
		String anchor = page.getWebURL().getAnchor();

		logger.debug("Docid: {}", docid);
		logger.info("URL: {}", url);
		logger.debug("Domain: '{}'", domain);
		logger.debug("Sub-domain: '{}'", subDomain);
		logger.debug("Path: '{}'", path);
		logger.debug("Parent page: {}", parentUrl);
		logger.debug("Anchor text: {}", anchor);

		handlePage(page);

		Header[] responseHeaders = page.getFetchResponseHeaders();
		if (responseHeaders != null) {
			logger.debug("Response headers:");
			for (Header header : responseHeaders) {
				logger.debug("\t{}: {}", header.getName(), header.getValue());
			}
		}

		logger.debug("=============");
	}

	private void handlePage(Page page) {
		logger.debug("=================== handlePageData =======================");
		HtmlParseData htmlParseData = null;
		if (page.getParseData() instanceof HtmlParseData) {
			htmlParseData = (HtmlParseData) page.getParseData();
		}

		String url = page.getWebURL().getURL();
		String text = htmlParseData.getText();
		String html = htmlParseData.getHtml();
		Set<WebURL> links = htmlParseData.getOutgoingUrls();

		logger.debug("Html URL: {}", url);
		logger.debug("Text length: {}", text.length());
		logger.debug("Html length: {}", html.length());
		logger.debug("Number of outgoing links: {}", links.size());
		String outputFile = Config.instance().getCrawlerStorageFolder() + File.separator
				+ Config.instance().getOutputFile();
		File file = null;
		BufferedWriter writer = null;
		try {
			file = new File(outputFile);
			if (!file.exists()) {
				file.createNewFile();
				logger.debug("Created output file {}", outputFile);
			}
			writer = new BufferedWriter(new FileWriter(file, true));
			writer.write("Page:\t" + url + "\n");

			for (WebURL link : links) {
				logger.debug("Sub Link: \t\t{}", link.getURL());
				writer.write("\t\tSub Link: \t" + link.getURL() + "\n");
			}
			writer.close();
		} catch (IOException e) {
			logger.error("Writting to output file {} failed.", outputFile);
		}
		logger.debug("==========================================================");
	}
}
