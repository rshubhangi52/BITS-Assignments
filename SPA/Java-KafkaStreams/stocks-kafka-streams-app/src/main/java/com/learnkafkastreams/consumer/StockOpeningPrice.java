package com.learnkafkastreams.consumer;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import com.opencsv.CSVReader;

/**
 * 
 */
public class StockOpeningPrice {

	private static Map<String, Double> openingPriceByInstrument = new HashMap<String, Double>(50);
	
	public static void loadOpeningPrice(String file) { 
		try { 
	        // Create an object of filereader 
	        // class with CSV file as a parameter. 
	        FileReader filereader = new FileReader(file); 
	        // create csvReader object passing 
	        // file reader as a parameter 
	        CSVReader csvReader = new CSVReader(filereader); 
	        String[] nextRecord; 
	        // we are going to read data line by line 
	        while ((nextRecord = csvReader.readNext()) != null) { 
	            openingPriceByInstrument.put(nextRecord[0], Double.parseDouble(nextRecord[1]));
		    } 
	    } 
	    catch (Exception e) { 
	        e.printStackTrace(); 
	    } 
	}
	
	public static Map<String, Double> getOpeningPriceByInstrument() { 
		return openingPriceByInstrument;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String csvFile = "C:\\spa\\BITSAssignments\\SPA\\opening_price.csv"; // Replace with your CSV file path
		loadOpeningPrice(csvFile);
	}

}
