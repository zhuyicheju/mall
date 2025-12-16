package com.example.mall.service.impl;

import com.example.mall.mapper.SeckillMapper;
import com.example.mall.service.OrderService;
import com.example.mall.service.DTO.SeckillOrderInsertDTO;

import jakarta.annotation.Resource;

public class OrderServiceImpl implements OrderService {
    @Resource
    SeckillMapper seckillMapper;
    
    public boolean isMessageProcessed(Long userId, Long goodId){
        int count = seckillMapper.countSeckillOrder(userId, goodId);
        return count > 0;
    }
    public void createOrder(Long userId, Long goodId){
        SeckillOrderInsertDTO seckillOrderInsertDTO = new SeckillOrderInsertDTO(userId, goodId);
        seckillMapper.insertSeckillOrder(seckillOrderInsertDTO);
    }    
}
