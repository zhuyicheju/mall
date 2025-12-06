package com.example.mall.service.Utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.example.mall.common.Utils.RedisUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SeckillRedisUtils {

    public static final String SECKILL_STOCK_KEY_PREFIX = "seckill:stock:";
    public static final String SECKILL_CONFIG_KEY_PREFIX = "seckill:config:";

    public static final String SECKILL_CONFIG_FIELD_START_TIME = "startTime";
    public static final String SECKILL_CONFIG_FIELD_END_TIME = "endTime";
    public static final String SECKILL_CONFIG_FIELD_LIMIT = "limit";

    @Autowired
    private RedisUtils redisUtils;

    private String buildSeckillStockKey(Long goodsId) {
        return SECKILL_STOCK_KEY_PREFIX + goodsId;
    }

    private String buildSeckillConfigKey(Long goodsId) {
        return SECKILL_CONFIG_KEY_PREFIX + goodsId;
    }

    private long calcutePreheatExpireTime(LocalDateTime seckillEndTime){
        LocalDateTime targetTime = seckillEndTime.plusHours(1);
        
        ZoneId zoneId = ZoneId.systemDefault();
        long targetTimestamp = targetTime.atZone(zoneId).toInstant().toEpochMilli();
        long currentTimestamp = System.currentTimeMillis();
        
        long remainingMillis = Math.max(0, targetTimestamp - currentTimestamp);
        
        long remainingSeconds = TimeUnit.MILLISECONDS.toSeconds(remainingMillis);
        return remainingSeconds;
    }

    public void setSeckillStock(Long goodsId, Long stock, Long expireTime) {
        String stockKey = buildSeckillStockKey(goodsId);

        redisUtils.set(stockKey, stock, expireTime, TimeUnit.SECONDS);
    }

    public void setSeckillConfig(Long goodsId, LocalDateTime startTime, LocalDateTime endTime, Integer limit, Long expireTime) {
        String configKey = buildSeckillConfigKey(goodsId);

        Map<String, Object> configMap = new HashMap<>(3);
        configMap.put(SECKILL_CONFIG_FIELD_START_TIME, startTime);
        configMap.put(SECKILL_CONFIG_FIELD_END_TIME, endTime);
        configMap.put(SECKILL_CONFIG_FIELD_LIMIT, limit);
        redisUtils.hashPutAll(configKey, configMap, expireTime, TimeUnit.SECONDS);
    }

    public void preheatSeckillGoods(Long goodsId, Long stock, LocalDateTime startTime, LocalDateTime endTime, Integer limit) {
        Long expireTime = calcutePreheatExpireTime(endTime); 

        if(expireTime == 0){
            log.info("秒杀活动已过期");
            return;
        }
    
        setSeckillStock(goodsId, stock, expireTime);
        setSeckillConfig(goodsId, startTime, endTime, limit, expireTime);
    }
}
