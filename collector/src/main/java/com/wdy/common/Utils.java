package com.wdy.common;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Utils {
	private static final Logger logger = LoggerFactory.getLogger(Utils.class);

	public static Set<String> search(String input, String parttenStr, int groupIndex) {
		Set<String> result = new HashSet<String>();
		Pattern pattern = Pattern.compile(parttenStr, Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
			if (groupIndex != 0) {
				result.add(matcher.group(groupIndex));
			} else {
				result.add(matcher.group());
			}
		}
		return result;
	}

	public static String extractInfo(String input, String pattern, int groupIndex) {
		return extractInfo(input, pattern, groupIndex, false);
	}

	public static String extractInfo(String input, String pattern, int groupIndex, boolean merge) {
		String result = "";
		Set<String> setStr = Utils.search(input, pattern, groupIndex);
		if (setStr.isEmpty()) {
			logger.warn("Not found any matching items for pattern '{}'", pattern);
			return result;
		}

		logger.debug("Found {} matching items for '{}'.", setStr.size(), pattern);
		for (String item : setStr) {
			logger.debug("Found matching item: {}", item);
			if (merge) {
				result = result.isEmpty() ? item : result + "\n" + item;
			} else {
				result = item;
			}
		}
		return result;
	}

	public static Collection<Method> getMethods(Class<?> clazz) {
		Collection<Method> found = new ArrayList<Method>();
		while (clazz != null) {
			for (Method m1 : clazz.getDeclaredMethods()) {
				boolean overridden = false;

				for (Method m2 : found) {
					if (m2.getName().equals(m1.getName())
							&& Arrays.deepEquals(m1.getParameterTypes(), m2.getParameterTypes())) {
						overridden = true;
						break;
					}
				}

				if (!overridden) {
					found.add(m1);
				}
			}
			clazz = clazz.getSuperclass();
		}

		return found;
	}
}
