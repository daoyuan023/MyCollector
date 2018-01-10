package com.wdy.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wdy.common.Constants;
import com.wdy.common.Utils;

public class SeedsInput {
	private static final Logger logger = LoggerFactory.getLogger(SeedsInput.class);

	public Set<String> getSeedsUrl() {
		String seedUrlFile = Constants.CFG_SEEDURL_FILE;
		Set<String> seeds = new HashSet<String>();
		try {
			String strPath = Utils.class.getClassLoader().getResource(seedUrlFile).getPath();
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
