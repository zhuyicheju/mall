package com.example.mall.common.Utils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

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
    
    public void hashPutAll(String key, Map<String, Object> configMap, Long timeout, TimeUnit timeUnit){
        redisTemplate.opsForHash().putAll(key, configMap);
        redisTemplate.expire(key, timeout, timeUnit);
    }

    // LUA脚本相关

    //直接执行lua脚本
    public <T> T executeLuaScript(String script, List<String> keys, Object[] args, Class<T> returnType){
        RedisScript<T> redisScript = new DefaultRedisScript<>(script, returnType);
        List<String> keyList = CollectionUtils.isEmpty(keys) ? Collections.emptyList() : keys;
        Object[] argArray = args == null ? new Object[0] : args;
        T result = redisTemplate.execute(redisScript, keyList, argArray);
        return result;
    }

    // 加载lua脚本，返回sha1
    public String loadLuaScript(String script){
        RedisCallback<String> callback = (connection) -> {
            Jedis jedis = (Jedis) connection.getNativeConnection();
            return jedis.scriptLoad(script);
        };    
        String sha1 = redisTemplate.execute(callback);
        return sha1;
    }

    public <T> T executeLuaBySha(
        String sha1,
        String script, //若sha1不存在则采用脚本
        List<String> keys,
        Object[] args,
        Class<T> returnType
    ){
        final String proxyScript =
            "return redis.call('evalsha', ARGV[1], #KEYS, unpak(KEYS), unpack(ARGV, 2))";
        RedisScript<T> redisScript = new DefaultRedisScript<>(proxyScript, returnType);
        Object[] evalShaArgs = new Object[(args == null ? 0 : args.length) + 1];
        evalShaArgs[0] = sha1;
        if (args != null) {
            System.arraycopy(args, 0, evalShaArgs, 1, args.length);
        }

        List<String> keyList = (keys == null ? Collections.emptyList() : keys);

        try {
            log.debug("Executing Redis SHA1 Script: sha={}, KEYS={}, ARGV={}", sha1, keyList, evalShaArgs);
            return redisTemplate.execute(redisScript, keyList, evalShaArgs);
        } catch (Exception ex) {
            // Detect NOSCRIPT error
            if (ex.getMessage() != null && ex.getMessage().contains("NOSCRIPT")) {
                log.warn("SHA1 script missing in Redis, fallback to EVAL => sha={}, error={}", sha1, ex.getMessage());

                RedisScript<T> evalScript = new DefaultRedisScript<>(script, returnType);
                return redisTemplate.execute(evalScript, keyList, args);
            }

            log.error("Redis evalsha execution failed - sha={}, KEYS={}, ARGV={}", sha1, keyList, evalShaArgs, ex);
            throw ex;
        }
    }

    public boolean tryLockNoValue(String key, long expireTime, TimeUnit timeUnit){
        boolean success = redisTemplate.opsForValue().setIfAbsent(key, 1, expireTime, timeUnit);
        return success;
    }

    public void releaseLockNoValue(String lockKey) {
        redisTemplate.delete(lockKey);
    }
}