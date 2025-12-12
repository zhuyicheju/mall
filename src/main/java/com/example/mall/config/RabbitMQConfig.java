package com.example.mall.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {
    private final static String ORDER_QUEUE = "order.create.queue";
    private final static String ORDER_EXCHANGE = "order.create.exchange";
    private final static String ORDER_ROUTING_KEY = "order.create.key";

    @Bean
    public DirectExchange orderExchange(){
        return new DirectExchange(ORDER_EXCHANGE, true, false);
    }

    @Bean
    public Queue orderQueue(){
        return new Queue(ORDER_QUEUE, true);
    }

    @Bean
    public Binding orderBinding(){
        return BindingBuilder
                .bind(orderQueue())
                .to(orderExchange())
                .with(ORDER_ROUTING_KEY);
    }
}
