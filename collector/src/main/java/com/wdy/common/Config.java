package com.wdy.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

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
    
	public Set<String> getSeedsUrl() {
		String seedUrlFile = Constants.CFG_SEEDURL_FILE;
		Set<String> seeds = new HashSet<String>();
		try {
			String strPath = Util.class.getClassLoader().getResource(seedUrlFile).getPath();
			File file = new File(strPath);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			while (line != null) {
				if (!line.startsWith("#") && !line.isEmpty()) {
					logger.debug("Seed Url: " + line);
					seeds.add(line);
				}
				line = br.readLine();
			}
		} catch (Exception e) {
			logger.error("Failed to read url seeds file " + seedUrlFile, e);
		}
		return seeds;
	}
}
