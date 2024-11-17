/**
 * 
 */
package com.learnkafkastreams.consumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Deque;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.google.gson.JsonParseException;
import com.learnkafkastreams.domain.StockData;
import com.learnkafkastreams.topology.OrdersTopology;

/**
 * 
 */
public class MaxProfitStockCalculator extends BaseStockCalculator {

	/**
	 * 
	 * @return
	 */
	public static void calculateMaxProfit() {
		String maxProfitInstrument = null;
	    double maxProfit = Double.MIN_VALUE;
	    double openingPrice = 0.0;
        for (Map.Entry<String, Deque<StockData>> entry : slidingWindow.entrySet()) {
	    	String instrument = entry.getKey();
	        Deque<StockData> dataDeque = entry.getValue();
	        if (dataDeque.size() < 2) continue; // Not enough data for a valid calculation
	        if (StockOpeningPrice.getOpeningPriceByInstrument().get(instrument) != null) {
	        	openingPrice = StockOpeningPrice.getOpeningPriceByInstrument().get(instrument);
	        }
	        double avgClosingPrice = dataDeque.stream().mapToDouble(d -> d.getPrice()).average().orElse(0.0);
	        double profit = avgClosingPrice - openingPrice;
            if (profit > maxProfit) {
            	maxProfit = profit;
                maxProfitInstrument = instrument;
            }
            if (maxProfitInstrument != null) {
            	System.out.printf("Instrument with Maximum Profit: %s, Profit: %.2f%n", maxProfitInstrument, maxProfit);
            	//Properties propsProducer = new Properties();
        		//propsProducer.put("bootstrap.servers", "localhost:9092");
        		//propsProducer.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        		//propsProducer.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        		//try (Producer<String, String> producer = new KafkaProducer<>(propsProducer)) {
    		    //	producer.send(new ProducerRecord<>(OrdersTopology.MAXPROFIT_TOPIC, "MAX_PROFIT_INSTRUMENT", maxProfitInstrument));
    		    //	producer.flush();
    		    //}
            }
	    }
	}    
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StockOpeningPrice.loadOpeningPrice("C:\\spa\\BITSAssignments\\SPA\\opening_price.csv");
		
		Properties propsConsumer = new Properties();
		propsConsumer.put("bootstrap.servers", "localhost:9092");
		propsConsumer.put("group.id", "profit_calculator");
		propsConsumer.put("key.deserializer", StringDeserializer.class.getName());
		propsConsumer.put("value.deserializer", StringDeserializer.class.getName());
		propsConsumer.put("auto.offset.reset", "earliest");
		
		try (Consumer<String, String> consumer = new KafkaConsumer<>(propsConsumer)) {
			consumer.subscribe(Collections.singletonList(OrdersTopology.STOCK_PRICE_TOPIC));

			while (true) {
			    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMinutes(TIME_LIMIT));

			    for (ConsumerRecord<String, String> consumerRecord : records) {
			        StockData data = gson.fromJson(consumerRecord.value(), StockData.class);
			        String instrument = data.getInstrument();
			        slidingWindow.computeIfAbsent(instrument, k -> new ConcurrentLinkedDeque<>()).addLast(data);
			        // Maintain a sliding window of 5 minutes for each instrument
			        while (isOutsideWindow(slidingWindow.get(instrument), data.getTimestamp())) {
			            slidingWindow.get(instrument).pollFirst();
			        }
			    }
			    calculateMaxProfit();
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
