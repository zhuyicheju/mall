package com.example.mall.service;

import java.util.Map;

public interface OrderConsumer {
    public void receive(Map<String, Object> msg);
}
