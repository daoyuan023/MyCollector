package com.wdy.io;

public class PageField {
	private String fieldName;
	private String pattern;
	private int matchIndex;
	private String url;

	public PageField(String fieldName, String pattern, int matchIndex, String url) {
		super();
		this.fieldName = fieldName;
		this.pattern = pattern;
		this.matchIndex = matchIndex;
		this.url = url;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getMatchIndex() {
		return matchIndex;
	}

	public void setMatchIndex(int matchIndex) {
		this.matchIndex = matchIndex;
	}
}
