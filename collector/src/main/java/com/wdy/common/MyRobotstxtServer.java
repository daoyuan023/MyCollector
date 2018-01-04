package com.wdy.common;

import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyRobotstxtServer extends RobotstxtServer {

    public MyRobotstxtServer(RobotstxtConfig config, PageFetcher pageFetcher) {
        super(config, pageFetcher);
    }

    @Override
    public boolean allows(WebURL webURL) {
        // ignore robots.txt temporarily for debug
        return true;
    }
}
