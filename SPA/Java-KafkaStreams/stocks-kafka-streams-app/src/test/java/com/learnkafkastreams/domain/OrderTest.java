package com.learnkafkastreams.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderTest {


    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);


    //@Test
    void orderDomainTest() throws JsonProcessingException {

    	var order = new Order("ABC", 1053.44,
        		1, "trader_1",
                "buy","2", "123", "456");

        var orderJSON = objectMapper.writeValueAsString(order);
        System.out.println("orderJSON :"+orderJSON);
        var expectedJSON = "{\"orderId\":12345,\"locationId\":\"store_1234\",\"finalAmount\":27.00,\"orderType\":\"GENERAL\",\"orderLineItems\":[{\"item\":\"Bananas\",\"count\":2,\"amount\":2.00},{\"item\":\"Iphone Charger\",\"count\":1,\"amount\":25.00}],\"orderedDateTime\":\"2022-12-05T08:55:27\"}";

        assertEquals(expectedJSON, orderJSON);
    }

    //@Test
    void orderRecordDomainTest() throws JsonProcessingException {
        
    	var order = new Order("ABC", 1053.44,
        		1, "trader_1",
                "buy","2", "123", "456");

        var orderJSON = objectMapper.writeValueAsString(order);
        System.out.println("orderJSON :"+orderJSON);
        var expectedJSON = "{\"orderId\":12345,\"locationId\":\"store_1234\",\"finalAmount\":27.00,\"orderType\":\"GENERAL\",\"orderLineItems\":[{\"item\":\"Bananas\",\"count\":2,\"amount\":2.00},{\"item\":\"Iphone Charger\",\"count\":1,\"amount\":25.00}],\"orderedDateTime\":\"2022-12-05T08:55:27\"}";

        assertEquals(expectedJSON, orderJSON);
    }

    //@Test
    void orderDomainRestaurantTest() throws JsonProcessingException {
    	
    	var order = new Order("ABC", 1053.44,
        		1, "trader_1",
                "buy","2", "123", "456");
        
        var orderJSON = objectMapper.writeValueAsString(order);
        System.out.println("orderJSON :"+orderJSON);
        var expectedJSON = "{\"orderId\":12345,\"locationId\":\"store_1234\",\"finalAmount\":15.00,\"orderType\":\"RESTAURANT\",\"orderLineItems\":[{\"item\":\"Pizza\",\"count\":2,\"amount\":12.00},{\"item\":\"Coffee\",\"count\":1,\"amount\":3.00}],\"orderedDateTime\":\"2022-12-05T08:55:27\"}";

        assertEquals(expectedJSON, orderJSON);
    }
}
