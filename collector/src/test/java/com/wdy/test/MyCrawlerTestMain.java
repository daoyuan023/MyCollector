package com.wdy.test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.wdy.common.Config;
import com.wdy.dao.house.HouseDataBaseOperator;
import com.wdy.house.HouseDataDaily;
import com.wdy.house.MarketArea;
import com.wdy.io.PageField;
import com.wdy.io.PageFieldManager;
import com.wdy.web.WebPage;
import com.wdy.web.WebPageCache;

public class MyCrawlerTestMain {
	private static final Logger logger = LoggerFactory.getLogger(MyCrawlerTestMain.class);

	public static void main(String[] args) {
		MyCrawlerTestMain tm = new MyCrawlerTestMain();
		// tm.testConfigProperties();
		// tm.testContains();
		// tm.testDB();
		// tm.testJson();
		// tm.testField();
		tm.testConfig();

	}

	void testConfig() {
		logger.debug("Resource location: {}", Config.instance().getResourceLocation());
	}

	void testField() {
		PageFieldManager pfm = new PageFieldManager();

		PageField pageField = pfm.getFieldInfo("lastMonthTrans");
		if (pageField == null) {
			logger.warn("Not found page field info of: {}", "lastMonthTrans");
			return;
		}
		WebPage webPage = WebPageCache.getWebPage(pageField.getUrl());
		webPage.getFieldAsInt(pageField.getPattern(), pageField.getMatchIndex());

	}

	void testJson() {
		PageFieldManager matchInfo = new PageFieldManager();
		matchInfo.getFieldInfo("totalHouseForSale");
	}

	void testConfigProperties() {
		logger.debug(Config.instance().get("user"));
	}

	void testContains() {
		Set<String> jobIDs = new HashSet<String>();
		logger.debug("{}", jobIDs.contains(""));
		logger.debug("{}", jobIDs.contains(null));
		jobIDs.add("hello");
		jobIDs.add("111");
		logger.debug("{}", jobIDs.contains(""));
		logger.debug("{}", jobIDs.contains(null));
	}

	void testDB() {

		queryTest();

		writeTest();

		queryTest();

	}

	public void writeTest() {
		HouseDataDaily houseData = new HouseDataDaily();
		houseData.setCity("Beijing");

		houseData.setAvgPriceDaily(62180);
		houseData.setTransAmountAgentLJ(236);
		houseData.setTransAmountOfficial(1121);
		houseData.setTotalForSale(40300);

		MarketArea area = new MarketArea();
		area.setAreaName("HuiLongGuan");
		area.setFilterConditions("250-340");
		area.setTotalForSale(125);
		area.setFilterForSale(30);

		try {
			houseData.getTargetAreas().add((MarketArea) (area.clone()));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		area.setAreaName("HuoYing");
		area.setFilterForSale(15);
		houseData.getTargetAreas().add(area);

		logger.debug("Insert one line data");

		HouseDataBaseOperator.instance().insert(houseData);
	}

	public void queryTest() {
		// Bson filter = Filters.where("this.time.length > 10 && this.date ==
		// '2018-07-03'");

		List<HouseDataDaily> houseDataList = HouseDataBaseOperator.instance().query();
		for (HouseDataDaily houseData : houseDataList) {
			logger.debug("LastUpdateTime: " + houseData.getLastUpdateTime() + ", House Number saled: "
					+ houseData.getTransAmountAgentLJ());
		}
	}

	public void updateTest() {
		HouseDataBaseOperator.instance().update(Filters.eq("_id", new ObjectId("5b3c8b7f2712ed84bccded46")),
				Updates.set("numDealAgentLJ", 139));
	}
}
