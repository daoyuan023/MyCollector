package com.wdy.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wdy.common.Config;


public class MyCrawlerTestMain {
    private static final Logger logger = LoggerFactory.getLogger(MyCrawlerTestMain.class);

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        MyCrawlerTestMain tm = new MyCrawlerTestMain();
        tm.testConfigProperties();

    }

    void testConfigProperties() {
        logger.debug(Config.getInstance().get("user"));
    }
}
