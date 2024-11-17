/**
 * 
 */
package com.learnkafkastreams.consumer;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.learnkafkastreams.domain.StockData;

/**
 * 
 */
public abstract class BaseStockCalculator {

	protected static final int WINDOW_SIZE = 5; // 5-minute sliding window
	
	protected static final int TIME_LIMIT = 10; // last 10-minutes
    
	protected static final Gson gson = new Gson();
	
	protected static final Map<String, Deque<StockData>> slidingWindow = new ConcurrentHashMap<>();
	
	protected static boolean isOutsideWindow(Deque<StockData> window, long currentTimestamp) {
        if (window.isEmpty()) return false;
        return currentTimestamp - window.peekFirst().getTimestamp() > WINDOW_SIZE * 60 * 1000; // 5 minutes in milliseconds
    }
   
}