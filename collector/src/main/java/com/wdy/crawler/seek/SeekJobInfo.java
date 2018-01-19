package com.wdy.crawler.seek;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wdy.common.Utils;
import com.wdy.io.DataBuffer;
import com.wdy.io.FileOutput;
import com.wdy.io.Output;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class SeekJobInfo extends WebCrawler {
	private static final Logger logger = LoggerFactory.getLogger(SeekJobInfo.class);
	private static final Pattern IMAGE_EXTENSIONS = Pattern.compile(".*\\.(bmp|gif|jpg|png)$");
	private final Set<String> jobIDs = new HashSet<String>();

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		logger.debug("wdy checking URL: " + url);

		if (!href.startsWith("https://www.seek.co.nz")) {
			return false;
		} else if (!href.contains("jobs-in-information-communication-technology")) {
			return false;
		} else if (IMAGE_EXTENSIONS.matcher(href).matches()) {
		
			// Ignore the url if it has an extension that matches our defined
			// set of image extensions.
			return false;
		}
		
		if(href.endsWith("jobs-in-information-communication-technology") ||
				href.matches(".*jobs-in-information-communication-technology.*page=\\d+")) {
			return true;
		}
		return false;
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

		HtmlParseData htmlParseData = null;
		if (page.getParseData() instanceof HtmlParseData) {
			htmlParseData = (HtmlParseData) page.getParseData();
		}

		logger.debug("wdy visiting URL: " + url);

		Set<WebURL> links = htmlParseData.getOutgoingUrls();
		for (WebURL link : links) {
			logger.debug("Sub Link: \t\t{}", link.getURL());
		}

		logger.debug("Docid: {}", docid);
		logger.info("URL: {}", url);
		logger.debug("Domain: '{}'", domain);
		logger.debug("Sub-domain: '{}'", subDomain);
		logger.debug("Path: '{}'", path);
		logger.debug("Parent page: {}", parentUrl);
		logger.debug("Anchor text: {}", anchor);

		logger.debug("Page HTML:\n" + htmlParseData.getHtml());

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

	/*
	 * Extract the job list
	 */
	private void extractJobArticle(String html, WebURL webUrl) {
		Document doc = Jsoup.parse(html);
		Elements articles = doc.select("article");
		if (articles == null) {
			return;
		}

		String jobTitle = "";
		String jobID = "";
		String jobLocation = "";
		String jobDetailsLink = "";
		String jobSummary = "";

		String url = webUrl.getURL();
		String siteUrl = jobDetailsLink = url.substring(0, url.indexOf(webUrl.getPath()));
		
		String jobNum = Utils.extractInfo(html, "<[^<>]*data-automation=\"totalJobsCount\"[^<>]*>(\\d+)<[^<>]+>", 1);
		logger.info("Found {} jobs.", jobNum);

		for (Element article : articles) {
			String articleHtml = article.html();
			// logger.info("Article html:\n{}", articleHtml);
			// logger.info("Article text:\n{}", article.text());

			// Job ID and Title
			jobTitle = article.attr("aria-label");
			jobID = article.attr("data-job-id");
			
			if (jobID.isEmpty() || jobIDs.contains(jobID)) {
				logger.info("The job {} is aready visited.", jobID);
				continue;
			} else {
				jobIDs.add(jobID);
			}

			// Job Location
			jobLocation = Utils.extractInfo(articleHtml, ">location:\\s*([^<>]*)</span>", 1);

			// Job details link
			jobDetailsLink = siteUrl + Utils.extractInfo(articleHtml, "\"(/job/[^\\s]+)\"", 1);

			// Job summary/short description
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

			extractJobDetails(jobDetailsLink, jobInfo);

			getDataBuffer().put(jobInfo);
		}
	}

	/*
	 * Open the job details link and extract the details info
	 */
	private JobInfo extractJobDetails(String url, JobInfo jobInfo) {
		try {
			Document doc = Jsoup.connect(url).get();
			String pageHtml = doc.html();
			// logger.debug("Job Details Html:\n{}", pageHtml);
			logger.info("====================== Job Information ======================");

			// Job type
			String jobType = Utils.extractInfo(pageHtml,
					"<dd data-automation=\"job-detail-work-type\">([^<>]*)<span([^<>]*)>([^<>]*)<span([^<>]*)>([^<>]*)</span>([^<>]*)</span>",
					5);
			jobInfo.setType(jobType);

			// Job listed date
			String jobDate = Utils.extractInfo(pageHtml,
					"<dd data-automation=\"job-detail-date\">([^<>]*)<span([^<>]*)>([^<>]*)<span([^<>]*)>([^<>]*)</span>([^<>]*)</span>",
					5);
			jobInfo.setListDate(jobDate);

			// Job details
			String jobDetails = doc.getElementsByClass("templatetext").html();
			jobDetails = jobDetails.replaceAll("<li>", "\n - ");
			jobDetails = jobDetails.replaceAll("<(p|br)>", "\n");

			jobDetails = jobDetails.replaceAll("<[^<>]+>", "");
			jobDetails = jobDetails.replaceAll("&nbsp;", "");
			
			jobInfo.setDetails(jobDetails);
		} catch (IOException e) {
			logger.error("Failed to connect {}: {}", url, e.getMessage());
		}
		return jobInfo;
	}
	
	private DataBuffer getDataBuffer() {
		Object obj = this.myController.getCustomData();
		if (obj instanceof DataBuffer) {
			return (DataBuffer) obj;
		}
		return null;
	}
}
