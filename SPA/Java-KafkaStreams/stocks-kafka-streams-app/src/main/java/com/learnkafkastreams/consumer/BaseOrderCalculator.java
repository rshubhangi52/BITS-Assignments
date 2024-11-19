/**
 * 
 */
package com.learnkafkastreams.consumer;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.learnkafkastreams.domain.TradeOrder;

/**
 * 
 */
public abstract class BaseOrderCalculator {

	protected static final int WINDOW_SIZE = 5; // 5-minute sliding window
    
	protected static final int HOP_BY = 1; // 1-minute hop
	
	protected static final int TIME_LIMIT = 10; //calculate for last 10-minutes
	
	protected static final Gson gson = new Gson();
	
	protected static final Map<String, Deque<TradeOrder>> slidingWindow = new ConcurrentHashMap<>();
	
	protected static boolean isOutsideWindow(Deque<TradeOrder> window, long currentTimestamp) {
        if (window.isEmpty()) return false;
        return currentTimestamp - window.peekFirst().getTimestamp() > WINDOW_SIZE * 60 * 1000; // 5 minutes in milliseconds
    }
   
}