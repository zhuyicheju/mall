package com.example.mall.common.Utils;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

@Component
public class RedisUtils {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value, long timeout, TimeUnit unit){
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteKey(String key){
        redisTemplate.delete(key);
    }

    public Set<String> findKeysWithPrefix(String prefix){
        return redisTemplate.keys(prefix + "*");
    }

    public void deleteKeys(Set<String> keys){
        redisTemplate.delete(keys);
    }
}
