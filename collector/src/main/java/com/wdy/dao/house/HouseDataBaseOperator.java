package com.wdy.dao.house;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wdy.dao.DataBaseOperator;
import com.wdy.house.HouseDataDaily;

public class HouseDataBaseOperator extends DataBaseOperator<HouseDataDaily> {
	private final static Logger logger = LoggerFactory.getLogger(HouseDataBaseOperator.class);

	private static final String COLLECTION_HOUSE = "housemarket";
	private static HouseDataBaseOperator operater = new HouseDataBaseOperator(COLLECTION_HOUSE);

	private HouseDataBaseOperator(String collectionName) {
		super(collectionName);
		logger.debug("Init operator on default collection: " + collectionName);
	}

	// Operator on default collection
	public static HouseDataBaseOperator instance() {
		return operater;
	}

	// Operator on specified collection
	public static HouseDataBaseOperator instance(String collectionName) {
		HouseDataBaseOperator tmpOperater = new HouseDataBaseOperator(collectionName);
		logger.debug("Get operator on collection: " + collectionName);
		return tmpOperater;
	}

	public static HouseDataBaseOperator monthlyData() {
		return instance("housemarket.monthly");
	}

	public static HouseDataBaseOperator dailyData() {
		return instance("housemarket.daily");
	}
}
