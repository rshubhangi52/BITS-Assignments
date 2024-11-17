package com.learnkafkastreams.topology;

import com.learnkafkastreams.domain.Order;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TopologyTestDriver;

import static com.learnkafkastreams.topology.OrdersTopology.ORDERS;

class OrdersTopologyTest {

    TopologyTestDriver topologyTestDriver = null;
    TestInputTopic<String, Order> ordersInputTopic = null;

    static String INPUT_TOPIC = ORDERS;



    /*static List<KeyValue<String, Order>> orders(){

    	var order1 = new Order("ABC", 1053.44,
        		1, "trader_1",
                "buy", "123", "456");
        
        var order2 = new Order("XYZ", 1053.44,
        		1, "trader_1",
                "sell", "123", "456");
        //var keyValue1 = KeyValue.pair( order1.().toString()
                , order1);

        //var keyValue2 = KeyValue.pair( order2.orderId().toString()
                , order2);


        //return  List.of(keyValue1, keyValue2);

    }*/
}