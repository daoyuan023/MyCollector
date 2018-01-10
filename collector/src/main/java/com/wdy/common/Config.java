package com.wdy.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
    private static final Logger logger = LoggerFactory.getLogger(Config.class);
    private final Properties prop;
    private final String configFile = Constants.CONFIG_FILE_NAME;
    private static final Config instance = new Config();

    private Config() {
        prop = new Properties();
        loadConfig();
    }

    public static Config instance() {
        return instance;
    }

    public Config reload() {
    	prop.clear();
    	loadConfig();
    	return instance;
    }
    
    private void loadConfig() {
        try {
            InputStream input = this.getClass().getClassLoader().getResourceAsStream(configFile);
            prop.load(input);

            for (String key : prop.stringPropertyNames()) {
                logger.debug("config item '{}', '{}'", key, get(key));
            }
        } catch (IOException e) {
            logger.error("Failed to load config file " + configFile, e);
        }
    }

    public String get(String key) {
        return prop.getProperty(key);
    }

    public Object getOrDefault(Object key, Object defaultValue) {
        return prop.getOrDefault(key, defaultValue);
    }

    public void set(String key, String value) {
        prop.setProperty(key, value);
    }
    
    public String getCrawlerStorageFolder() {
    	return get(Constants.CFG_CRAWL_STORAGE_FOLDER);
    }
    
    public int getCrawlerNum() {
    	return Integer.parseInt(getOrDefault(Constants.CFG_CRAWLERS_NUMBERS, "5").toString());
    }
    
    public int getMaxPageNum() {
    	return Integer.parseInt(getOrDefault(Constants.CFG_MAX_PAGE_NUM, "1000").toString());
    }
    
    public String getCrawlerClassName() {
    	return get(Constants.CFG_CRAWLER);
    }
    
    public String getOutputFile() {
    	return get(Constants.CFG_OUTPUT_FILE);
    }
}
