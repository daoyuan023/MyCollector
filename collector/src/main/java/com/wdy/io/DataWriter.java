package com.wdy.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataWriter implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(SeedsInput.class);
	private DataBuffer dataBuffer;
	
	public DataWriter(DataBuffer dataBuffer) {
		this.dataBuffer = dataBuffer;
	}
	
	@Override
	public void run() {
		Output op = new FileOutput();
		
		while (true) {
			Object data = dataBuffer.get();
			if (data != null) {
				op.write(data);
			} else {
				logger.error("Null data from buffer.");
			}
		}
	}
}
