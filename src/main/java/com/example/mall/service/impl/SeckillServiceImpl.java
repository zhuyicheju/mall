package com.example.mall.service.impl;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.mall.mapper.GoodsMapper;
import com.example.mall.mapper.SeckillMapper;
import com.example.mall.service.SeckillService;
import com.example.mall.service.DTO.SeckillConfigAndStockDTO;
import com.example.mall.service.Utils.SeckillRedisUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SeckillServiceImpl implements SeckillService {

    @Resource
    private SeckillMapper seckillMapper;

    @Resource
    private SeckillRedisUtils seckillRedisUtil;
    // stock key
    // config key
    // user has bought key

    private final static String REDIS_STOCK_KEY = "seckill:stock:";
    private final static String REDIS_CONFIG_KEY = "seckill:config:";

    private final static Integer NOT_IN_SECKILL = 0;

    public void preheatGood(Long id){
        String redisStockKey = REDIS_STOCK_KEY + id;
        String redisConfigKey = REDIS_CONFIG_KEY + id;

        SeckillConfigAndStockDTO seckillConfigAndStockDTO = seckillMapper.getSeckillConfigAndStock(id);
        if (seckillConfigAndStockDTO.getIsSeckill() == NOT_IN_SECKILL){
            return;
        }

        seckillRedisUtil.preheatSeckillGoods(id, seckillConfigAndStockDTO.getStock(),
                                                 seckillConfigAndStockDTO.getSeckillStartTime(),
                                                 seckillConfigAndStockDTO.getSeckillEndTime(),
                                                 seckillConfigAndStockDTO.getSeckillLimit());
        
    }

    public void preheatGoods(Set<Long> ids){
        for(Long id : ids){
            preheatGood(id);
        }
    }

    public void preheatAllSeckillGoods(){
        Set<Long> goods = getSeckillGoods();
        preheatGoods(goods);
    }
    
    public Set<Long> getSeckillGoods(){
        Set<Long> goods = seckillMapper.getSeckillGoods();
        return goods;
    }
}
