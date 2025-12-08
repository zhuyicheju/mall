package com.example.mall.service.Utils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.example.mall.common.MallException;
import com.example.mall.common.Utils.RedisUtils;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SeckillRedisUtils {

    public static final String SECKILL_STOCK_KEY_PREFIX = "seckill:stock:";
    public static final String SECKILL_CONFIG_KEY_PREFIX = "seckill:config:";
    private static final String USER_KEY_PREFIX = "seckill:users:";

    public static final String SECKILL_CONFIG_FIELD_START_TIME = "startTime";
    public static final String SECKILL_CONFIG_FIELD_END_TIME = "endTime";
    public static final String SECKILL_CONFIG_FIELD_LIMIT = "limit";

    @Autowired
    private RedisUtils redisUtils;

    @Value("${path.lua_script}")
    private String luaScriptPath;

    private String luaSha1;

    private String luaScript;

    @PostConstruct
    private void loadLuaScript(){
        try{
            luaScript = Files.readString(
                Paths.get(luaScriptPath),
                StandardCharsets.UTF_8
            );
            luaSha1 = redisUtils.loadLuaScript(luaScript);
        }catch(Exception e){
            log.error("lua加载失败");
        }
    }

    public Integer checkAndReserveStock(Long userId, Long goodId){
        String stockKey = buildSeckillStockKey(goodId);
        String userKey = buildSeckillUserKey(goodId);
        List<String> keys = Arrays.asList(stockKey, userKey);
        Object[] args = {userId};
        Integer ret = redisUtils.<Integer>executeLuaBySha(luaSha1, luaScript, keys, args, Integer.class);
        return ret;
    }

    public String buildSeckillUserKey(Long goodsId) {
        return USER_KEY_PREFIX + goodsId;
    }

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
