package com.learnkafkastreams.consumer;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.opencsv.CSVReader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StockOpeningPrice {

	private static Map<String, Double> openingPriceByInstrument = new HashMap<String, Double>(50);
	
	public static void loadOpeningPrice(String file) { 
		CSVReader csvReader = null;
		try { 
	        // Create an object of filereader 
	        // class with CSV file as a parameter. 
	        FileReader filereader = new FileReader(file); 
	        // create csvReader object passing 
	        // file reader as a parameter 
	        csvReader = new CSVReader(filereader); 
	        String[] nextRecord; 
	        // we are going to read data line by line 
	        while ((nextRecord = csvReader.readNext()) != null) { 
	        	log.info("{}, {}", nextRecord[0], nextRecord[1]);
	        	openingPriceByInstrument.put(nextRecord[0], Double.parseDouble(nextRecord[1]));
		    } 
	    } 
	    catch (Exception e) { 
	        e.printStackTrace(); 
	    } finally {
	    	if (csvReader != null) {
	    		try {
					csvReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
		}
	}
	
	public static Map<String, Double> getOpeningPriceByInstrument() { 
		return openingPriceByInstrument;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String csvFile = "src/main/resources/opening_price.csv";
		loadOpeningPrice(csvFile);
	}

}
