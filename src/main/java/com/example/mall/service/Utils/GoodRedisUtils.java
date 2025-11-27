package com.example.mall.service.Utils;

import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.example.mall.common.PageResponse;
import com.example.mall.common.Utils.RedisUtils;
import com.example.mall.controller.dto.GoodDetailDTO;
import com.example.mall.controller.dto.GoodSimpleDTO;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GoodRedisUtils {
    @Resource
    RedisUtils redisUtils;

    private static final ScheduledExecutorService DELAY_DELETE_POOL = Executors.newSingleThreadScheduledExecutor(runnable -> {
        Thread thread = new Thread(runnable, "goods-redis-delay-delete");
        thread.setDaemon(true);
        return thread;
    });

    private final static String GOOD = "good:";
    private final static String DETAIL = "detail:";
    private final static String LIST = "list:";
    private final static String ID = "id=";
    private final static String PAGE = "page=";
    private final static String PAGESIZE = "pagesize=";
    private final static String REDIS_NULL = "NULL";

    private final static int DETAIL_EXPIRE_TIME = 3600;
    private final static int LIST_EXPIRE_TIME = 600;
    private final static int NULL_EXPIRE_TIME = 60;
    private final static int DOUBLE_DELETE_TIME = 60;

    private long randomTime(){
        long random = (long) (Math.random() * 30);
        log.debug("randomTime - 生成随机时间: {}", random);
        return random;
    }

    private String buildGoodDetailKey(Long id){
        String key = GOOD + DETAIL + ID + id; 
        log.debug("buildGoodDetailKey - 构建商品详情Key: id={}, key={}", id, key);
        return key;
    }

    private String buildGoodListKey(int page, int pagesize){
        String key = GOOD + LIST + PAGE + page + ":" + PAGESIZE + pagesize;
        log.debug("buildGoodListKey - 构建商品列表Key: page={}, pagesize={}, key={}", page, pagesize, key);
        return key;
    }

    public void setDetail(Long id, GoodDetailDTO goodDetailDTO){
        log.info("setDetail - 开始: id={}, goodDetailDTO={}", id, goodDetailDTO);
        String key = buildGoodDetailKey(id);
        if (goodDetailDTO == null){
            log.debug("setDetail - 商品详情为null，设置NULL标识");
            redisUtils.set(key, REDIS_NULL, NULL_EXPIRE_TIME + randomTime(), TimeUnit.SECONDS);
        } else {
            redisUtils.set(key, goodDetailDTO, DETAIL_EXPIRE_TIME + randomTime(), TimeUnit.SECONDS);
        }
        log.info("setDetail - 结束: key={}设置完成", key);
    }

    public void setList(int page, int pagesize, PageResponse<GoodSimpleDTO> pageResult){
        log.info("setList - 开始: page={}, pagesize={}, pageResult={}", page, pagesize, pageResult);
        String key = buildGoodListKey(page, pagesize);
        int expireTime = LIST_EXPIRE_TIME;
        if (pageResult.getData() == null){
           expireTime = NULL_EXPIRE_TIME;
           log.debug("setList - 商品列表数据为null，设置过期时间为{}", expireTime);
        } 
        redisUtils.set(key, pageResult, expireTime + randomTime(), TimeUnit.SECONDS);
        log.info("setList - 结束: key={}设置完成", key);
    }

    public GoodDetailDTO getDetail(Long id){
        log.info("getDetail - 开始: id={}", id);
        String key = buildGoodDetailKey(id);
        GoodDetailDTO goodDetailDTO = (GoodDetailDTO) redisUtils.get(key);
        log.info("getDetail - 结束: id={}, 结果={}", id, goodDetailDTO);
        return goodDetailDTO;
    }

    
    public PageResponse<GoodSimpleDTO> getList(int page, int pagesize){
        log.info("getList - 开始: page={}, pagesize={}", page, pagesize);
        String key = buildGoodListKey(page, pagesize);
        PageResponse<GoodSimpleDTO> goodListDTO = (PageResponse<GoodSimpleDTO>) redisUtils.get(key);
        log.info("getList - 结束: page={}, pagesize={}, 结果={}", page, pagesize, goodListDTO);
        return goodListDTO;
    }

    public void deleteDetail(Long id) {
        log.info("deleteDetail - 开始: id={}", id);
        String key = buildGoodDetailKey(id);
        redisUtils.deleteKey(key);
        log.info("deleteDetail - 结束: id={}, key={}已删除", id, key);
    }

    public void deleteAllListCache() {
        log.info("deleteAllListCache - 开始");
        Set<String> keys = redisUtils.findKeysWithPrefix(GOOD+LIST);
        log.debug("deleteAllListCache - 找到匹配的Key数量: {}", keys.size());
        redisUtils.deleteKeys(keys);
        log.info("deleteAllListCache - 结束: 删除{}个Key", keys.size());
    }

    public void doubleDelete(Long id){
        log.info("doubleDelete - 开始: id={}", id);
        deleteDetail(id);
        deleteAllListCache();
        DELAY_DELETE_POOL.schedule( () -> {
            log.info("doubleDelete - 执行延迟删除任务: id={}", id);
            try {
                deleteDetail(id);
                deleteAllListCache();
                log.info("doubleDelete - 延迟删除任务执行完成: id={}", id);
            } catch (Exception e) {
                log.error("doubleDelete - 延迟双删失败", e);
            }
        }, DOUBLE_DELETE_TIME, TimeUnit.SECONDS);
        log.info("doubleDelete - 结束: 延迟删除任务已提交");
    }
}