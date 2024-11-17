package com.learnkafkastreams.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.learnkafkastreams.domain.Order;
import com.learnkafkastreams.domain.OrderType;
import com.learnkafkastreams.topology.OrdersTopology;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.apache.kafka.clients.producer.RecordMetadata;

import static com.learnkafkastreams.producer.ProducerUtil.publishMessageSync;
import static java.lang.Thread.sleep;

@Slf4j
public class OrdersMockDataProducer {

    public static void main(String[] args) throws InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        publishOrders(objectMapper, buildOrders());
    }
    
/*
 * stock_name: str
    price: float
    quantity: int
    name: str
    type: str                    # "buy" or "sell"
    valid_till: str
    timestamp: str
 * 
 */
    
    
    private static List<Order> buildOrders() {
        
        var order1 = new Order("ABC", 100.0,
        		1, "trader_11",
                "buy","1", "123", "456");
        
        var order2 = new Order("ABC", 100.0,
        		1, "trader_12",
                "sell","2", "123", "457");
        
        var order3 = new Order("GHI", 200.0,
        		2, "trader_4",
                "sell","3", "123", "459");
        
        var order4 = new Order("XYZ", 200.0,
        		2, "trader_13",
                "buy","4", "123", "460");
        
        var order5 = new Order("XYZ", 200.0,
        		2, "trader_14",
                "sell","5", "123", "459");

        return List.of(
                order1,
                order2,
                order3,
                order4,
                order5
        );
    }
    
    /*private static void publishOrders(ObjectMapper objectMapper, List<Order> orders) {

        orders
                .forEach(order -> {
                    try {
                        var ordersJSON = objectMapper.writeValueAsString(order);
                        var recordMetaData = publishMessageSync(OrdersTopology.ORDERS, order.orderId() + "", ordersJSON);
                        log.info("Published the order message : {} ", recordMetaData);
                    } catch (JsonProcessingException e) {
                        log.error("JsonProcessingException : {} ", e.getMessage(), e);
                        throw new RuntimeException(e);
                    } catch (Exception e) {
                        log.error("Exception : {} ", e.getMessage(), e);
                        throw new RuntimeException(e);
                    }
                });
    }*/

    private static void publishOrders(ObjectMapper objectMapper, List<Order> orders) {

        orders
                .forEach(order -> {
                    try {
                    	 
                        var ordersJSON = objectMapper.writeValueAsString(order);
                        var recordMetaData = publishMessageSync(OrdersTopology.ORDERS, order.order_id() + "", ordersJSON);
                        log.info("Published the order message : {} ", recordMetaData);
                        
                        // Publish message to appropriate Kafka topic based on order type
                        /*if ("buy".equals(order.type())) {
                            recordMetaData = publishMessageSync(OrdersTopology.BUY_ORDERS, order.stock_name()+"", ordersJSON);
                        } else if ("sell".equals(order.type())) {
                            recordMetaData = publishMessageSync(OrdersTopology.SELL_ORDERS, order.stock_name()+"", ordersJSON);
                        } else {
                            log.warn("Unknown order type: {}", order.type());
                            return;
                        }*/
                        
                        // Log the successful message publication
                        log.info("Published the order message : {} ", recordMetaData);
                    } catch (JsonProcessingException e) {
                        log.error("JsonProcessingException : {} ", e.getMessage(), e);
                        throw new RuntimeException(e);
                    } catch (Exception e) {
                        log.error("Exception : {} ", e.getMessage(), e);
                        throw new RuntimeException(e);
                    }
                });
    }


}
