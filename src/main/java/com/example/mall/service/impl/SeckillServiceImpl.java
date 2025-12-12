package com.example.mall.service.impl;

import java.util.Set;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.example.mall.common.MallException;
import com.example.mall.common.Utils.RedisUtils;
import com.example.mall.common.enums.HttpResultCode;
import com.example.mall.entity.SeckillOrder;
import com.example.mall.mapper.SeckillMapper;
import com.example.mall.service.SeckillService;
import com.example.mall.service.DTO.SeckillConfigAndStockDTO;
import com.example.mall.service.DTO.SeckillOrderInsertDTO;
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

    private void createSeckillOrder(Long userId, Long goodsId) {
        try {
            seckillMapper.insertSeckillOrder(new SeckillOrderInsertDTO(userId, goodsId));
        } catch (DuplicateKeyException e) {
            // 捕获唯一索引冲突，说明已下单
            log.warn("用户{}重复购买商品{}", userId, goodsId);
            throw new MallException(HttpResultCode.BAD_REQUEST,"已参与秒杀，请勿重复提交");
        }
    }
}
