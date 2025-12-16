package com.example.mall.service;

public interface OrderService {
    public boolean isMessageProcessed(Long userId, Long goodId);
    public void createOrder(Long userId, Long goodId);
}
