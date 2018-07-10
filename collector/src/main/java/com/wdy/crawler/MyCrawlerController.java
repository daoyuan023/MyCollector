package com.wdy.crawler;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wdy.common.Config;
import com.wdy.common.MyRobotstxtServer;
import com.wdy.io.DataBuffer;
import com.wdy.io.DataWriter;
import com.wdy.io.SeedsReader;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class MyCrawlerController {
	private static final Logger logger = LoggerFactory.getLogger(MyCrawlerController.class);

	public static void main(String[] args) throws Exception {
		String crawlStorageFolder = Config.instance().getCrawlerStorageFolder();
		if (crawlStorageFolder == null || crawlStorageFolder.isEmpty()) {
			logger.error("No valid crawl storage folder is provided in config file.");
			return;
		}

		logger.debug(crawlStorageFolder);
		int numberOfCrawlers = Config.instance().getCrawlerNum();

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setPolitenessDelay(1000);

		config.setMaxDepthOfCrawling(5);
		config.setMaxPagesToFetch(Config.instance().getMaxPageNum());

		config.setIncludeBinaryContentInCrawling(false);
		config.setResumableCrawling(false);
		
		config.setRespectNoIndex(false);

		logger.debug("Crawler Config:\n{}", config.toString());
		
		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new MyRobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

		SeedsReader seedsInput = new SeedsReader();
		Set<String> seeds = seedsInput.getSeedsUrl();
		for (String url : seeds) {
			logger.debug("Adding Seed URL: " + url);
			controller.addSeed(url);
		}

		initDataBuffer(controller);
		
		Class<?> crawlerClass = Class.forName(Config.instance().getCrawlerClassName());
		logger.info("Start crawler: " + crawlerClass.getName());
		
		controller.start(crawlerClass.asSubclass(WebCrawler.class), numberOfCrawlers);
	}

	private static void initDataBuffer(CrawlController controller) {
		DataBuffer dataBuffer = new DataBuffer();
		controller.setCustomData(dataBuffer);
		
		DataWriter dataWriter = new DataWriter(dataBuffer);
		Thread dataWriterThread = new Thread(dataWriter, "DataWriterThread");
		dataWriterThread.start();
	}
}
