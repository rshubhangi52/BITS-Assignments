/**
 * 
 */
package com.learnkafkastreams.consumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Deque;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.google.gson.JsonParseException;
import com.learnkafkastreams.domain.Order;
import com.learnkafkastreams.topology.OrdersTopology;;

/**
 * 
 */
public class SMAOrderCalculator extends BaseOrderCalculator {
	
	public static double calculateSMA(String instrument) {
        Deque<Order> dataDeque = slidingWindow.get(instrument);
        if (dataDeque.size() < 2) return 0.0; // Not enough data for a valid calculation
        return (dataDeque.stream().mapToDouble(d -> d.price()).average().orElse(0.0));
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "sma_order_calculator");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        props.put("auto.offset.reset", "earliest");
        
        Properties propsProducer = new Properties();
        propsProducer.put("bootstrap.servers", "localhost:9092");
        propsProducer.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        propsProducer.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        
        try (Consumer<String, String> consumer = new KafkaConsumer<>(props)) {
			consumer.subscribe(Collections.singletonList(OrdersTopology.MATCHED_TRADES_TOPIC));
			while (true) {
			    ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(60*10));
			    for (ConsumerRecord<String, String> consumerRecord : records) {
			        Order data = gson.fromJson(consumerRecord.value(), Order.class);
			        String instrument = data.stock_name();
			        slidingWindow.computeIfAbsent(instrument, k -> new ConcurrentLinkedDeque<>()).addLast(data);

			        // Maintain a sliding window of 5 minutes for each instrument
			        while (isOutsideWindow(slidingWindow.get(instrument), Long.parseLong(data.timestamp()))) {
			            slidingWindow.get(instrument).pollFirst();
			        }
			        System.out.printf("Instrument: %s, SMA (5-min): %.2f%n", instrument, calculateSMA(instrument));
			    }
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
