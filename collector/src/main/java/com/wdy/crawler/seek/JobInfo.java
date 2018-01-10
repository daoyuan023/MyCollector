package com.wdy.crawler.seek;

public class JobInfo {
	private String title;
	private String summary;
	private String detailsLink;
	private String listDate;
	private String location;
	private String notes;
	private String id;
	private String type;
	private String details;

	public JobInfo(String title, String summary, String detailsLink, String listDate, String location, String notes,
			String id, String type, String details) {
		super();
		this.title = title;
		this.summary = summary;
		this.detailsLink = detailsLink;
		this.listDate = listDate;
		this.location = location;
		this.notes = notes;
		this.id = id;
		this.type = type;
		this.details = details;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public JobInfo() {

	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDetailsLink() {
		return detailsLink;
	}

	public void setDetailsLink(String detailsLink) {
		this.detailsLink = detailsLink;
	}

	public String getListDate() {
		return listDate;
	}

	public void setListDate(String listDate) {
		this.listDate = listDate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
