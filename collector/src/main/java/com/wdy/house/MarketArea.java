package com.wdy.house;

public class MarketArea extends Data {
	private String areaName;
	private String filterConditions;
	private int totalForSale;
	private int filterForSale;
	private int minPrice;
	private int maxPrice;
	
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getFilterConditions() {
		return filterConditions;
	}
	public void setFilterConditions(String filterConditions) {
		this.filterConditions = filterConditions;
	}
	public int getTotalForSale() {
		return totalForSale;
	}
	public void setTotalForSale(int totalForSale) {
		this.totalForSale = totalForSale;
	}
	public int getFilterForSale() {
		return filterForSale;
	}
	public void setFilterForSale(int filterForSale) {
		this.filterForSale = filterForSale;
	}
	public int getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(int minPrice) {
		this.minPrice = minPrice;
	}
	public int getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(int maxPrice) {
		this.maxPrice = maxPrice;
	}
}
