package com.example.mall.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.example.mall.service.OrderProducer;

import jakarta.annotation.Resource;

@Service
public class OrderProducerImpl implements OrderProducer{

    private final static String ORDER_EXCHANGE = "order.create.exchange";
    private final static String ORDER_ROUTING_KEY = "order.create.key";

    @Resource
    RabbitTemplate rabbitTemplate;


    public void send(Long userId, Long goodId){
        Map<String, Object> message = new HashMap<>();
        message.put("userId", userId);
        message.put("goodId", goodId);
        rabbitTemplate.convertAndSend(
            ORDER_EXCHANGE,
            ORDER_ROUTING_KEY,
            message
        );
    }
}
