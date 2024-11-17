/**
 * 
 */
package com.learnkafkastreams.domain;

/**
 * 
 */
public class StockData {

	private String instrument;
    private double price;
    private long timestamp;
    
    public StockData() {
    	// Blank Constructor
    }
   
	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
