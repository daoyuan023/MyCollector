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
    private static final Config instantce = new Config();

    private Config() {
        prop = new Properties();
        loadConfig();
    }

    public static Config getInstance() {
        return instantce;
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
}
