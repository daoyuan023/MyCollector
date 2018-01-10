package com.wdy.io;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wdy.common.Utils;

public abstract class Output {
	private static final Logger logger = LoggerFactory.getLogger(Output.class);

	protected List<Map<String, String>> extractDataRecord(List<Object> datas) {
		List<Map<String, String>> records = new ArrayList<Map<String, String>>();
		for (Object ob : datas) {
			records.add(extractDataRecord(ob));
		}
		return records;
	}

	protected Map<String, String> extractDataRecord(Object data) {
		Collection<Method> methods = Utils.getMethods(data.getClass());
		Map<String, String> records = new HashMap<String, String>();
		try {
			for (Method method : methods) {
				String menthodName = method.getName();
				logger.debug("menthod name: {}", menthodName);
				if (menthodName.startsWith("get") && !menthodName.equals("getClass")) {
					String field = menthodName.replaceFirst("get", "");
					Object objValue = method.invoke(data);
					String value = objValue == null ? "" : objValue.toString();
					logger.debug("Record: '{}', '{}'", field, value);
					records.put(field, value);
				}
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			logger.error("Failed to extract data record from {}: {}", data.getClass(), e.getMessage());
		}
		return records;
	}

	public void write(Object obj) {
		Map<String, String> data = extractDataRecord(obj);
		logger.info("=============== " + obj.getClass().getSimpleName() + " ===============");
		for (Entry<String, String> entry : data.entrySet()) {
			logger.info(entry.getKey() + ": " + entry.getValue());
		}
	}

	public void write(List<Object> objs) {
		for (Object obj : objs) {
			write(obj);
		}
	}
}
