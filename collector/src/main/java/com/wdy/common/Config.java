package com.wdy.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
	private static final Logger logger = LoggerFactory.getLogger(Config.class);
	private final String configFile = Constants.CONFIG_FILE_NAME;
	private final Properties prop;
	private String resourceLocation = null;

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
			InputStream input = this.getResource(configFile);
			prop.load(input);
			input.close();

			for (String key : prop.stringPropertyNames()) {
				logger.debug("config item '{}', '{}'", key, get(key));
			}
		} catch (IOException e) {
			logger.error("Failed to load config file " + configFile, e);
		}
	}

	public InputStream getResource(String filename) {
		InputStream inputStream = null;
		try {
			if (resourceLocation != null) {
				String path = resourceLocation + File.separator + filename;
				File file = new File(path);

				if (file == null || !file.exists()) {
					logger.warn("Not found the file in specified location: {}, using default class loader.",
							resourceLocation);
					inputStream = getClass().getClassLoader().getResourceAsStream(filename);
				} else {
					inputStream = new FileInputStream(file);
				}
			} else {
				inputStream = getClass().getClassLoader().getResourceAsStream(filename);
			}

			logger.debug("Get resource: {}", filename);
		} catch (Exception e) {
			logger.error("Failed to get resource'{}': {}", filename, e.getMessage());
		}

		return inputStream;
	}

	public void setResourceLocation(String resourceLocation) {
		this.resourceLocation = resourceLocation;
	}

	public String get(String key) {
		return prop.getProperty(key);
	}

	public long getAsLong(String key) {
		long value = 0;
		String tmp = null;
		try {
			tmp = get(key);
			value = Long.parseLong(tmp);
		} catch (Exception e) {
			logger.warn("Unexpected format{}: {}", e.getMessage(), tmp);
		}
		return value;
	}
	
	public int getAsInt(String key) {
		int value = 0;
		String tmp = null;
		try {
			tmp = get(key);
			value = Integer.parseInt(tmp);
		} catch (Exception e) {
			logger.warn("Unexpected format{}: {}", e.getMessage(), tmp);
		}
		return value;
	}
	
	public double getAsDouble(String key) {
		double value = 0;
		String tmp = null;
		try {
			tmp = get(key);
			value = Double.parseDouble(tmp);
		} catch (Exception e) {
			logger.warn("Unexpected format{}: {}", e.getMessage(), tmp);
		}
		return value;
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

	public String getResourceLocation() {
		return resourceLocation;
	}
}
