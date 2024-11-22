# SPA Assignment 2

## Project Overview
This project is focused on building a system to handle **order matching**, **moving averages**, and **profit calculation** using **Apache Kafka** with **Python** and **Java Kafka Streams**. The project is divided into two main parts:

1. **Python Producer-Consumer**: Handles the creation of Kafka topics, partitions, and produces order data.
2. **Java Kafka Streams**: Processes the incoming order data, performs matching logic, and computes moving averages and profit calculations.

## Folder Structure
```bash
SPA/
├── Python-Producer_Consumer/
│   ├── StockOrderProducer.ipynb
│   ├── KafkaConsumer.ipynb
└── Java-KafkaStreams/
    ├── OrdersKafkaStreamApp.java
    ├── OrdersTopology.java
    ├── SMAStockCalculator.java
    └── MaxProfitStockCalculator
```

## 1. Python-Producer_Consumer

### Description
This folder contains Python scripts to set up the Kafka environment and produce order data to a Kafka topic.

### Files
- **`StockOrderProducer.ipynb`**: 
  - Script to create the `orders` Kafka topic with partitions.
  - Ensures the topic is set up correctly before producing data.
- **`KafkaConsumer.ipynb`**:
  - Generates order data with a 70% match rate for stock symbols and traders.
  - Produces data to the Kafka `orders` topic.

## 2. Java-KafkaStreams

### Description
This folder contains Java programs using Kafka Streams for real-time data processing.

### Files
- **`OrdersKafkaStreamApp.java`**:
  - Code for Main function. 
- **`OrdersTopology.java`**:
  - Code for streaming data through topics and perform operations.
- **`SMAOrderCalculator.java`**:
  - Code for SMA calculation of instrument prices from matched trades
- **`MaxProfitOrderCalculator.java`**:
  - Code for instrument giving max profit from matched trades ions.
- **`SMAStockCalculator.java`**:
  - Code for SMA calculation of randomly generated stock prices
- **`MaxProfitOrderCalculator.java`**:
  - Code for instrument giving max profit from randomly generated stock prices
