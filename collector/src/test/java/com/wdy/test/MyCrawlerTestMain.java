package com.wdy.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wdy.common.Config;


public class MyCrawlerTestMain {
    private static final Logger logger = LoggerFactory.getLogger(MyCrawlerTestMain.class);

    public static void main(String[] args) {
        MyCrawlerTestMain tm = new MyCrawlerTestMain();
        tm.testConfigProperties();

    }

    void testConfigProperties() {
        logger.debug(Config.instance().get("user"));
    }
}
