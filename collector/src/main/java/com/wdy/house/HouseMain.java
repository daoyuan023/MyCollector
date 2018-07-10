package com.wdy.house;

import com.wdy.common.Config;

public class HouseMain {
	private final static String COLLECT_INTERVAL = "house.collect.interval";

	public static void main(String[] args) {
		loop();
	}

	private static void loop() {
		MarketDataCollector collector = new MarketDataCollector();
		long seconds = Config.instance().getAsLong(COLLECT_INTERVAL);
		while (true) {
			collector.collect();
			try {
				Thread.sleep(seconds*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
	}
}
