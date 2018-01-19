package com.wdy.io;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wdy.crawler.MyCrawler;

public class DataBuffer {
	private static final Logger logger = LoggerFactory.getLogger(MyCrawler.class);
	private  BlockingDeque<Object> dataQueue = new LinkedBlockingDeque<Object>();

	public void put(Object data) {
		try {
			dataQueue.put(data);
		} catch (InterruptedException e) {
			logger.error("Put operation is interrupted: {}", e.getMessage());
		}
	}
	
	public Object get() {
		try {
			return dataQueue.take();
		} catch (InterruptedException e) {
			logger.error("Take operation is interrupted: {}", e.getMessage());
		}
		return null;
	}
}
