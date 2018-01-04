package com.wdy.mycrawler;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wdy.common.Config;
import com.wdy.common.Constants;
import com.wdy.common.MyRobotstxtServer;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class MyCrawlerController {
	private static final Logger logger = LoggerFactory.getLogger(MyCrawlerController.class);

	public static void main(String[] args) throws Exception {

		String crawlStorageFolder = Config.getInstance().get(Constants.CFG_CRAWL_STORAGE_FOLDER);
		if (crawlStorageFolder == null || crawlStorageFolder.isEmpty()) {
			logger.error("No crawl storage folder is provided. Please check the {}",
					Constants.CFG_CRAWL_STORAGE_FOLDER);
			return;
		}

		logger.debug(crawlStorageFolder);
		int numberOfCrawlers = Integer
				.parseInt(Config.getInstance().getOrDefault(Constants.CFG_CRAWLERS_NUMBERS, "3").toString());

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setPolitenessDelay(1000);

		config.setMaxDepthOfCrawling(2);
		config.setMaxPagesToFetch(
				Integer.parseInt(Config.getInstance().getOrDefault(Constants.CFG_MAX_PAGE_NUM, "1000").toString()));

		config.setIncludeBinaryContentInCrawling(false);
		config.setResumableCrawling(false);

		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new MyRobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

		Set<String> seeds = Config.getInstance().getSeedsUrl();
		for (String url : seeds) {
			logger.debug("Adding URL Seed: " + url);
			controller.addSeed(url);
		}

		controller.start(MyCrawler.class, numberOfCrawlers);
	}
}
