package com.wdy.test;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wdy.common.Config;


public class MyCrawlerTestMain {
    private static final Logger logger = LoggerFactory.getLogger(MyCrawlerTestMain.class);

    public static void main(String[] args) {
        MyCrawlerTestMain tm = new MyCrawlerTestMain();
        tm.testConfigProperties();
        tm.testContains();

    }

    void testConfigProperties() {
        logger.debug(Config.instance().get("user"));
    }
    
    void testContains() {
    	Set<String> jobIDs = new HashSet<String>();
    	logger.debug("{}", jobIDs.contains(""));
    	logger.debug("{}", jobIDs.contains(null));
    	jobIDs.add("hello");
    	jobIDs.add("111");
    	logger.debug("{}", jobIDs.contains(""));
    	logger.debug("{}", jobIDs.contains(null));
    }
}
