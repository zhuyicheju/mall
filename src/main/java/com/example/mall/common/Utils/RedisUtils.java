package com.example.mall.common.Utils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RedisUtils {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value, long timeout, TimeUnit unit){
        log.info("set - 开始: key={}, value={}, timeout={}, unit={}", key, value, timeout, unit);
        redisTemplate.opsForValue().set(key, value, timeout, unit);
        log.info("set - 结束: Redis设置成功");
    }

    public Object get(String key) {
        log.info("get - 开始: key={}", key);
        Object result = redisTemplate.opsForValue().get(key);
        log.info("get - 结束: key={}, 结果={}", key, result);
        return result;
    }

    public void deleteKey(String key){
        log.info("deleteKey - 开始: key={}", key);
        redisTemplate.delete(key);
        log.info("deleteKey - 结束: key={}删除完成", key);
    }

    public Set<String> findKeysWithPrefix(String prefix){
        log.info("findKeysWithPrefix - 开始: prefix={}", prefix);
        Set<String> keys = redisTemplate.keys(prefix + "*");
        log.info("findKeysWithPrefix - 结束: prefix={}, 找到{}个key", prefix, keys.size());
        return keys;
    }

    public void deleteKeys(Set<String> keys){
        log.info("deleteKeys - 开始: keys数量={}", keys.size());
        redisTemplate.delete(keys);
        log.info("deleteKeys - 结束: 批量删除{}个key完成", keys.size());
    }
    
    public void hashPutAll(String key, Map<String, Object> configMap){
        redisTemplate.opsForHash().putAll(key, configMap);
    }

}