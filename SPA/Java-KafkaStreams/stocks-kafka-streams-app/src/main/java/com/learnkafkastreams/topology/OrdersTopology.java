package com.learnkafkastreams.topology;

import java.time.Duration;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.StreamJoined;

import com.learnkafkastreams.domain.Order;
import com.learnkafkastreams.serdes.SerdesFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrdersTopology {
    public static final String MATCHED_TRADES_TOPIC = "trades";
    public static final String ORDERS = "orders";
    public static final String STOCK_PRICE_TOPIC = "stock_prices";
    public static final String MAXPROFIT_TOPIC = "maxprofit";
    public static final String SMA_TOPIC = "sma";
    
    public static Topology buildTopology() {
    	StreamsBuilder streamsBuilder = new StreamsBuilder();
    	
    	
    	var orderStreams = streamsBuilder
                .stream(ORDERS,
                        Consumed.with(Serdes.String(), SerdesFactory.orderSerdes())
                );
    	orderStreams
        .print(Printed.<String, Order>toSysOut().withLabel("order:"));
    	
    	KStream<String, Order> buyOrders = orderStreams.filter((key, order) -> order.type().equals("buy"));
    	KStream<String, Order> sellOrders = orderStreams.filter((key, order) -> order.type().equals("sell"));
    	
    	buyOrders.join(
    			sellOrders,
    	        OrdersTopology::matchOrders, 
    	        JoinWindows.of(Duration.ofSeconds(10)),
    	        StreamJoined.with(Serdes.String(), SerdesFactory.orderSerdes(), SerdesFactory.orderSerdes())
    	    )
    	    .filter((key, value) -> value != null) 
    	    .peek((key, value) -> System.out.println("Matched Trade: Key = " + key + ", Value = " + value))
    	    .to(MATCHED_TRADES_TOPIC, Produced.with(Serdes.String(), SerdesFactory.orderSerdes()));
    	
    	return streamsBuilder.build();
    }
    
    private static Order matchOrders(Order buyOrder, Order sellOrder) {
        // Ensure both orders have the same stock name, price, and quantity
        if (!buyOrder.stock_name().equals(sellOrder.stock_name())) {
            return null; // No match if stock names don't match
        }

        if (buyOrder.price() < sellOrder.price()) {
            return null; // No match if buy price < sell price
        }

        if (buyOrder.quantity() != sellOrder.quantity()) {
            return null; // No match if quantities don't match
        }
        
        //if (Math.abs(Long.parseLong(buyOrder.timestamp()) - Long.parseLong(sellOrder.timestamp())) < 1) return null; 

        System.out.println("Matching Buy Order: " + buyOrder.name() + ": " + buyOrder.order_id() + " with Sell Order: " + sellOrder.name()  + ": " + sellOrder.order_id());

        // Create a matched trade with the full quantity
        return new Order(
            buyOrder.stock_name(),
            buyOrder.price(),
            buyOrder.quantity(),
            buyOrder.name() + "/" + sellOrder.name(),
            "trade",
            buyOrder.order_id() + "/" + sellOrder.order_id(),
            buyOrder.valid_till(),
            buyOrder.timestamp()
        );
    }

}
