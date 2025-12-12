package com.example.mall.mapper;

import java.util.Set;

import org.apache.ibatis.annotations.Mapper;

import com.example.mall.entity.SeckillOrder;
import com.example.mall.service.DTO.SeckillConfigAndStockDTO;
import com.example.mall.service.DTO.SeckillOrderInsertDTO;

@Mapper
public interface SeckillMapper {
    public SeckillConfigAndStockDTO getSeckillConfigAndStock(Long id);
    public Set<Long> getSeckillGoods();
    public void insertSeckillOrder(SeckillOrderInsertDTO seckillOrderInsertDTO);
}
