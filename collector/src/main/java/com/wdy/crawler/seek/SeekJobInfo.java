package com.wdy.crawler.seek;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wdy.common.Utils;
import com.wdy.io.FileOutput;
import com.wdy.io.Output;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class SeekJobInfo extends WebCrawler {
	private static final Logger logger = LoggerFactory.getLogger(SeekJobInfo.class);
	private static final Pattern IMAGE_EXTENSIONS = Pattern.compile(".*\\.(bmp|gif|jpg|png)$");

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		logger.debug("wdy checking URL: " + url);

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
		logger.debug("wdy visiting URL: " + url);

		logger.debug("Docid: {}", docid);
		logger.info("URL: {}", url);
		logger.debug("Domain: '{}'", domain);
		logger.debug("Sub-domain: '{}'", subDomain);
		logger.debug("Path: '{}'", path);
		logger.debug("Parent page: {}", parentUrl);
		logger.debug("Anchor text: {}", anchor);

		HtmlParseData htmlParseData = null;
		if (page.getParseData() instanceof HtmlParseData) {
			htmlParseData = (HtmlParseData) page.getParseData();
		}

		// logger.debug("HTML text:\n" + htmlParseData.getHtml());

		Header[] responseHeaders = page.getFetchResponseHeaders();
		if (responseHeaders != null) {
			logger.debug("Response headers:");
			for (Header header : responseHeaders) {
				logger.debug("\t{}: {}", header.getName(), header.getValue());
			}
		}

		logger.debug("=========================================");

		extractJobArticle(htmlParseData.getHtml(), page.getWebURL());
	}

	private void extractJobArticle(String html, WebURL webUrl) {
		Document doc = Jsoup.parse(html);
		Elements articles = doc.select("article");

		String jobTitle = "";
		String jobID = "";
		String jobLocation = "";
		String jobDetailsLink = "";
		String jobSummary = "";

		for (Element article : articles) {
			String articleHtml = article.html();
			logger.info("Article html:\n{}", articleHtml);
			// logger.info("Article text:\n{}", article.text());

			jobTitle = article.attr("aria-label");
			jobID = article.attr("data-job-id");

			jobLocation = Utils.extractInfo(articleHtml, ">location:\\s*([^<>]*)</span>", 1);

			String url = webUrl.getURL();
			jobDetailsLink = url.substring(0, url.indexOf(webUrl.getPath()));
			jobDetailsLink += Utils.extractInfo(articleHtml, "\"(/job/[^\\s]+)\"", 1);

			jobSummary = Utils.extractInfo(articleHtml,
					"<span data-automation=\"jobShortDescription\"([^<>]*)><span([^<>]*)><span([^<>]*)>([^<>]*)</span>",
					4);

			String jobKeyPoint = Utils.extractInfo(articleHtml, ">([^<>]*)</span></span></span></li>", 1, true);
			// String companyInfo = Util.extractPatternInfo(articleHtml,
			// ">([^<>]*)</span></span></span></li>", 1);

			JobInfo jobInfo = new JobInfo();

			jobInfo.setId(jobID);
			jobInfo.setTitle(jobTitle);
			jobInfo.setLocation(jobLocation);
			jobInfo.setDetailsLink(jobDetailsLink);
			jobInfo.setSummary(jobSummary + "\n" + jobKeyPoint);

			jobInfo = extractJobDetails(jobDetailsLink, jobInfo);

			Output op = new FileOutput();
			op.write(jobInfo);

			//
		}
	}

	private JobInfo extractJobDetails(String url, JobInfo jobInfo) {
		try {
			Document doc = Jsoup.connect(url).get();
			String pageHtml = doc.html();
			logger.debug("Job Details Html:\n{}", pageHtml);
			logger.info("====================== Job Information ======================");

			String jobType = Utils.extractInfo(pageHtml,
					"<dd data-automation=\"job-detail-work-type\">([^<>]*)<span([^<>]*)>([^<>]*)<span([^<>]*)>([^<>]*)</span>([^<>]*)</span>",
					5);
			jobInfo.setType(jobType);

			String jobDate = Utils.extractInfo(pageHtml,
					"<dd data-automation=\"job-detail-date\">([^<>]*)<span([^<>]*)>([^<>]*)<span([^<>]*)>([^<>]*)</span>([^<>]*)</span>",
					5);
			jobInfo.setListDate(jobDate);

			String jobDetails = "";
			jobInfo.setDetails(jobDetails);
		} catch (IOException e) {
			logger.error("Failed to connect {}: {}", url, e.getMessage());
		}
		return jobInfo;
	}
}
