package com.wdy.io;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wdy.common.Config;
import com.wdy.common.Constants;

public class SeedsReader {
	private static final Logger logger = LoggerFactory.getLogger(SeedsReader.class);

	public Set<String> getSeedsUrl() {
		String seedUrlFile = Constants.CFG_SEEDURL_FILE;
		Set<String> seeds = new HashSet<String>();
		try {
			InputStream inputStream = Config.instance().getResource(seedUrlFile);
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufReader = new BufferedReader(inputStreamReader);
			
			String line = bufReader.readLine();
			while (line != null) {
				if (!line.startsWith("#") && !line.isEmpty()) {
					logger.debug("Seed Url: " + line);
					seeds.add(line);
				}
				line = bufReader.readLine();
			}
			bufReader.close();
			inputStreamReader.close();
			inputStream.close();
		} catch (Exception e) {
			logger.error("Failed to read url seeds file " + seedUrlFile, e);
		}
		return seeds;
	}
}
