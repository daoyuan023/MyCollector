package com.wdy.house;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class HouseDataDaily extends Data {
	private String lastUpdateTime;
	private int year;
	private int month;
	private int day;
	private String city;
	
	private int transAmountAgentLJ;
	private int transAmountOfficial;
	private int totalForSale;
	private int avgPriceDaily;
	private int customerAmount;
	private int showAmount;
	private int houseAmountChanged;
	
	private List<MarketArea> targetAreas;
	
	public HouseDataDaily() {
		LocalDateTime currentTime = LocalDateTime.now();
		lastUpdateTime = currentTime.toString();

		year = currentTime.getYear();
		month = currentTime.getMonthValue();
		day = currentTime.getDayOfMonth();
		
		targetAreas = new ArrayList<MarketArea>();
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

	public int getTotalForSale() {
		return totalForSale;
	}

	public void setTotalForSale(int totalForSale) {
		this.totalForSale = totalForSale;
	}

	public int getAvgPriceDaily() {
		return avgPriceDaily;
	}

	public void setAvgPriceDaily(int avgPriceDaily) {
		this.avgPriceDaily = avgPriceDaily;
	}

	public int getCustomerAmount() {
		return customerAmount;
	}

	public void setCustomerAmount(int customerAmount) {
		this.customerAmount = customerAmount;
	}

	public int getShowAmount() {
		return showAmount;
	}

	public void setShowAmount(int showAmount) {
		this.showAmount = showAmount;
	}

	public int getHouseAmountChanged() {
		return houseAmountChanged;
	}

	public void setHouseAmountChanged(int houseAmountChanged) {
		this.houseAmountChanged = houseAmountChanged;
	}

	public List<MarketArea> getTargetAreas() {
		return targetAreas;
	}

	public void setTargetAreas(List<MarketArea> targetAreas) {
		this.targetAreas = targetAreas;
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

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}
}
