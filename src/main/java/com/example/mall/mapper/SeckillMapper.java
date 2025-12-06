package com.example.mall.mapper;

import java.util.Set;

import org.apache.ibatis.annotations.Mapper;

import com.example.mall.service.DTO.SeckillConfigAndStockDTO;

@Mapper
public interface SeckillMapper {
    public SeckillConfigAndStockDTO getSeckillConfigAndStock(Long id);
    public Set<Long> getSeckillGoods();
}
