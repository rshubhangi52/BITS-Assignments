/**
 * 
 */
package com.learnkafkastreams.producer;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.google.gson.Gson;
import com.learnkafkastreams.domain.TradeOrder;
import com.learnkafkastreams.topology.OrdersTopology;

/**
 * 
 */
public class TradeOrderProducer {

	private static final String[] INSTRUMENTS = {
			"AAPL",  // Apple
			"MSFT",  // Microsoft
			"GOOGL", // Alphabet (Google)
			"AMZN",  // Amazon
			"TSLA",  // Tesla
			"NVDA",  // NVIDIA
			"META",  // Meta Platforms (formerly Facebook)
			"BRK-B", // Berkshire Hathaway
			"JPM",   // JPMorgan Chase
			"V",     // Visa
			"PG",    // Procter & Gamble
			"UNH",   // UnitedHealth Group
			"JNJ",   // Johnson & Johnson
			"WMT",   // Walmart
			"MA",    // Mastercard
			"DIS",   // Walt Disney
			"PYPL",  // PayPal
			"HD",    // Home Depot
			"BAC",   // Bank of America
			"XOM",   // ExxonMobil
			"CVX",   // Chevron
			"LLY",   // Eli Lilly
			"ABT",   // Abbott Laboratories
			"CRM",   // Salesforce
			"KO",    //Coca-Cola
		};
	
    private static final Random RANDOM = new Random();
	
    /**
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException {
		Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        try (Producer<String, String> producer = new KafkaProducer<>(props)) {
			Gson gson = new Gson();
			while (true) {
			    for (String instrument : INSTRUMENTS) {
			    	
			    	TradeOrder tradeOrder = new TradeOrder();
			    	tradeOrder.setInstrument(instrument);
			    	tradeOrder.setPrice(100 + RANDOM.nextDouble() * 100);
			    	tradeOrder.setTimestamp(System.currentTimeMillis());
			    	producer.send(new ProducerRecord<>(OrdersTopology.MATCHED_TRADES_TOPIC, instrument, gson.toJson(tradeOrder)));
			    }
			    producer.flush();
			    TimeUnit.SECONDS.sleep(60); // Simulate real-time data (1 minute interval)
			}
		}
	}

}
