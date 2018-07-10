package com.wdy.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wdy.common.Config;

public class PageFieldManager {
	private static final Logger logger = LoggerFactory.getLogger(PageFieldManager.class);

	private static final String PF_JSON_FILE_NAME = "page_fields.json";
	private static final String PF_FIELD_NAME = "field_name";
	private static final String PF_MATCH_INDEX = "match_index";
	private static final String PF_URL = "URL";
	private static final String PF_PATTERN = "pattern";
	private static final String PF_PAGES = "pages";
	private static final String PF_FIELDS = "fields";
	private static final String PF_SCHEMA = "field_schema";

	private static Map<String, PageField> fieldInfoMap;

	public PageFieldManager() {
		if (fieldInfoMap == null) {
			fieldInfoMap = loadFieldsInfo();
		}
	}

	public void reLoad() {
		fieldInfoMap.clear();
		fieldInfoMap = loadFieldsInfo();
	}

	public PageField getFieldInfo(String fieldName) {
		return fieldInfoMap.get(fieldName);
	}

	private Map<String, PageField> loadFieldsInfo() {
		Map<String, PageField> fieldInfoMap = new HashMap<String, PageField>();
		String confFile = PF_JSON_FILE_NAME;
		try {
			// Load Json file to Json object
			String jsonStr = readResourceFile(confFile);
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonStr);

			JsonObject root = jsonObject.get(PF_SCHEMA).getAsJsonObject();
			for (JsonElement page : root.getAsJsonArray(PF_PAGES)) {
				// Extract fields info of page
				List<PageField> listPageField = extractPageField(page.getAsJsonObject());
				for (PageField pageField : listPageField) {
					fieldInfoMap.put(pageField.getFieldName(), pageField);
				}
			}
		} catch (Exception e) {
			logger.error("Error reading: " + confFile, e);
		}
		return fieldInfoMap;
	}

	// Read a file and return whole content as String
	private String readResourceFile(String fileName) {
		StringBuilder content = new StringBuilder();
		InputStream inputStream = Config.instance().getResource(fileName);
		InputStreamReader inputStreamReader = null;
		BufferedReader bufReader = null;
		try {
			inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			bufReader = new BufferedReader(inputStreamReader);

			String line = null;
			while ((line = bufReader.readLine()) != null) {
				content.append(line);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("UnsupportedEncodingException of {}: {}", fileName, e.getMessage());
		} catch (IOException e) {
			logger.error("Failed to read {}: {}", fileName, e.getMessage());
		} finally {
			try {
				bufReader.close();
				inputStreamReader.close();
				inputStream.close();
			} catch (Exception e) {
				logger.error("Failed to close: {}", e.getMessage());
			}
		}

		return content.toString();
	}

	private List<PageField> extractPageField(JsonObject jsonPage) {
		List<PageField> listPageField = new ArrayList<PageField>();
		try {
			String url = jsonPage.get(PF_URL).getAsString();
			logger.debug("Found URL: " + url);
			for (JsonElement jsonField : jsonPage.getAsJsonArray(PF_FIELDS)) {
				JsonObject jsonObject = jsonField.getAsJsonObject();
				String name = jsonObject.get(PF_FIELD_NAME).getAsString();
				String pattern = jsonObject.get(PF_PATTERN).getAsString();
				int matchIndex = jsonObject.get(PF_MATCH_INDEX).getAsInt();

				logger.debug("Found field, name: {}, pattern: {}, matchIndex: {} ", name, pattern, matchIndex);
				listPageField.add(new PageField(name, pattern, matchIndex, url));
			}
		} catch (Exception e) {
			logger.error("Failed to extract page field, please ensure json file format is expected, {}",
					e.getMessage());
		}
		return listPageField;
	}
}
