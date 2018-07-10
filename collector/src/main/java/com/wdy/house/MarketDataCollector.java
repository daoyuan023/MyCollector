package com.wdy.house;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.client.model.Filters;
import com.wdy.dao.house.HouseDataBaseOperator;
import com.wdy.io.PageField;
import com.wdy.io.PageFieldManager;
import com.wdy.web.WebPage;
import com.wdy.web.WebPageCache;

public class MarketDataCollector {
	private static final Logger logger = LoggerFactory.getLogger(MarketDataCollector.class);

	public MarketDataCollector() {

	}

	public void collect() {
		collectMonthlyData();
		collectDailyData();
	}
	
	private void collectMonthlyData() {
		PageFieldManager pfm = new PageFieldManager();
		PageField pageField = null;
		WebPage webPage = null;
		
		pageField = pfm.getFieldInfo("lastMonth");
		webPage = WebPageCache.getWebPage(pageField.getUrl());
		int lastMonth = webPage.getFieldAsInt(pageField.getPattern(), pageField.getMatchIndex());

		pageField = pfm.getFieldInfo("yearOfLastMonth");
		webPage = WebPageCache.getWebPage(pageField.getUrl());
		int yearOfLastMonth = webPage.getFieldAsInt(pageField.getPattern(), pageField.getMatchIndex());
		
		pageField = pfm.getFieldInfo("lastMonthAvgPrice");
		webPage = WebPageCache.getWebPage(pageField.getUrl());
		int lastMonthAvgPrice = webPage.getFieldAsInt(pageField.getPattern(), pageField.getMatchIndex());

		HouseDataMonthly houseData = new HouseDataMonthly();
		houseData.setCity("Beijing");
		houseData.setMonth(lastMonth);
		houseData.setYear(yearOfLastMonth);
		houseData.setAvgPriceMonthly(lastMonthAvgPrice);
		
		logger.debug("yearOfLastMonth: {}, lastMonth: {}, lastMonthAvgPrice: {}",
					 yearOfLastMonth, lastMonth, lastMonthAvgPrice);
		HouseDataBaseOperator.monthlyData().insert(houseData);
	}
	
	private void collectDailyData() {
		PageFieldManager pfm = new PageFieldManager();
		PageField pageField = null;
		WebPage webPage = null;
		
		pageField = pfm.getFieldInfo("totalHouseForSale");
		webPage = WebPageCache.getWebPage(pageField.getUrl());
		int totalForSale = webPage.getFieldAsInt(pageField.getPattern(), pageField.getMatchIndex());
		
		pageField = pfm.getFieldInfo("monthOfLastDay");
		webPage = WebPageCache.getWebPage(pageField.getUrl());
		int monthOfLastDay = webPage.getFieldAsInt(pageField.getPattern(), pageField.getMatchIndex());

		pageField = pfm.getFieldInfo("lastDay");
		webPage = WebPageCache.getWebPage(pageField.getUrl());
		int lastDay = webPage.getFieldAsInt(pageField.getPattern(), pageField.getMatchIndex());
		
		pageField = pfm.getFieldInfo("showAmount");
		webPage = WebPageCache.getWebPage(pageField.getUrl());
		int showAmount = webPage.getFieldAsInt(pageField.getPattern(), pageField.getMatchIndex());
		
		pageField = pfm.getFieldInfo("customerAmount");
		webPage = WebPageCache.getWebPage(pageField.getUrl());
		int customerAmount = webPage.getFieldAsInt(pageField.getPattern(), pageField.getMatchIndex());
		
		pageField = pfm.getFieldInfo("houseAmountChanged");
		webPage = WebPageCache.getWebPage(pageField.getUrl());
		int houseAmountChanged = webPage.getFieldAsInt(pageField.getPattern(), pageField.getMatchIndex());

				
		HouseDataDaily houseData = new HouseDataDaily();
		LocalDateTime today = LocalDateTime.now();
		
		int yearOfLastDay = today.minusDays(1).getYear();
		houseData = getHouseDataByDate(yearOfLastDay, monthOfLastDay, lastDay);
		if (houseData == null) {
			houseData = new HouseDataDaily();
		}
		houseData.setYear(yearOfLastDay);
		houseData.setMonth(monthOfLastDay);
		houseData.setDay(lastDay);
		houseData.setLastUpdateTime(today.toString());
		houseData.setCity("Beijing");
		
		houseData.setTotalForSale(totalForSale);
		houseData.setTotalForSale(totalForSale);
		houseData.setMonth(monthOfLastDay);
		houseData.setDay(lastDay);
		houseData.setShowAmount(showAmount);
		houseData.setCustomerAmount(customerAmount);
		houseData.setHouseAmountChanged(houseAmountChanged);
		
		HouseDataBaseOperator.dailyData().insert(houseData);
	}
	
	private HouseDataDaily getHouseDataByDate(int year, int month, int day) {
		Bson filter = Filters.and(Filters.eq("year", year),
								  Filters.eq("month", month),
								  Filters.eq("day", day));
		List<HouseDataDaily> dailyData = HouseDataBaseOperator.dailyData().query(filter);
		if (dailyData == null || dailyData.isEmpty()) {
			return null;
		}
		
		return dailyData.get(0);
	}
}
