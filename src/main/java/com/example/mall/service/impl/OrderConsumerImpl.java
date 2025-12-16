package com.example.mall.service.impl;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import com.example.mall.service.OrderConsumer;
import com.example.mall.service.OrderService;

import jakarta.annotation.Resource;

public class OrderConsumerImpl implements OrderConsumer {

    @Resource
    OrderService orderService;

    private final static String ORDER_QUEUE = "order.create.queue";

    @RabbitListener(queues = ORDER_QUEUE)
    public void receive(Map<String, Object> msg){
        Long userId = (Long) msg.get("userId");
        Long goodId = (Long) msg.get("goodId");
        if(orderService.isMessageProcessed(userId, goodId)){
            return;
        }

        orderService.createOrder(userId, goodId);
    }
}
