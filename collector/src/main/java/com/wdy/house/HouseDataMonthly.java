package com.wdy.house;

import java.time.LocalDateTime;

public class HouseDataMonthly extends Data {
	private String lastUpdateTime;
	private int year;
	private int month;
	private String city;

	private int transAmountAgentLJ;
	private int transAmountOfficial;
	private int avgPriceMonthly;
	
	public HouseDataMonthly() {
		LocalDateTime currentTime = LocalDateTime.now();
		lastUpdateTime = currentTime.toString();

		year = currentTime.getYear();
		month = currentTime.getMonthValue();
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getTransAmountAgentLJ() {
		return transAmountAgentLJ;
	}

	public void setTransAmountAgentLJ(int transAmountAgentLJ) {
		this.transAmountAgentLJ = transAmountAgentLJ;
	}

	public int getTransAmountOfficial() {
		return transAmountOfficial;
	}

	public void setTransAmountOfficial(int transAmountOfficial) {
		this.transAmountOfficial = transAmountOfficial;
	}

	public int getAvgPriceMonthly() {
		return avgPriceMonthly;
	}

	public void setAvgPriceMonthly(int avgPriceMonthly) {
		this.avgPriceMonthly = avgPriceMonthly;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}
}
