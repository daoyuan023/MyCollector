package com.wdy.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wdy.common.Config;
import com.wdy.crawler.MyCrawler;

public class FileOutput extends Output {
	private static final Logger logger = LoggerFactory.getLogger(MyCrawler.class);

	private File outputFile;

	public FileOutput() {
		createOutputFile(Config.instance().getOutputFile());
	}

	public FileOutput(String fileName) {
		createOutputFile(fileName);
	}
	public void createOutputFile(String fileName) {
		String filePath = Config.instance().getCrawlerStorageFolder() + File.separator + fileName;
		outputFile = new File(filePath);

		try {
			if (!outputFile.exists()) {
				outputFile.createNewFile();
			}
			logger.debug("Created output file {}", outputFile);
		} catch (IOException e) {
			logger.error("Failed to create output file {}: {}", filePath, e.getMessage());
		}
	}

	public void writeToOutputFile(String content) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(outputFile, true));
			writer.write(content + "\n");
			writer.close();
		} catch (IOException e) {
			logger.error("Writting to output file {} failed.", outputFile);
		}
	}

	public void write(Object obj) {
		Map<String, String> data = extractDataRecord(obj);
		writeToOutputFile("=============== " + obj.getClass().getSimpleName() + " ===============");
		for (Entry<String, String> entry : data.entrySet()) {
			writeToOutputFile(entry.getKey() + ": " + entry.getValue());
		}
	}
}
