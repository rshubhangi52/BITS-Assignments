package com.learnkafkastreams.domain;

public record Order(String stock_name,
					Double price, 
					int quantity,
					String name,
					String type,
					String order_id,
					String valid_till,
					String timestamp) {
}
