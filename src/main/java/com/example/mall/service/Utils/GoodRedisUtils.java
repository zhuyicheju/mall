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

    private final static int DETAIL_EXPIRE_TIME = 3600;
    private final static int LIST_EXPIRE_TIME = 600;
    private final static int NULL_EXPIRE_TIME = 60;
    private final static int DOUBLE_DELETE_TIME = 60;

    private long randomTime(){
        return (long) (Math.random() * 30);
    }

    private String buildGoodDetailKey(Long id){
        String key = GOOD + DETAIL + ID + id; 
        return key;
    }

    private String buildGoodListKey(int page, int pagesize){
        String key = GOOD + LIST + PAGE + page + ":" + PAGESIZE + pagesize;
        return key;
    }

    public void setDetail(Long id, GoodDetailDTO goodDetailDTO){
        String key = buildGoodDetailKey(id);
        if (goodDetailDTO == null){
            redisUtils.set(key, goodDetailDTO, NULL_EXPIRE_TIME + randomTime(), TimeUnit.SECONDS);
        }
        redisUtils.set(key, goodDetailDTO, DETAIL_EXPIRE_TIME + randomTime(), TimeUnit.SECONDS);
    }

    public void setList(int page, int pagesize, Object pageResult){
        String key = buildGoodListKey(page, pagesize);
        redisUtils.set(key, pageResult, LIST_EXPIRE_TIME + randomTime(), TimeUnit.SECONDS);
    }

    public GoodDetailDTO geDetail(Long id){
        String key = buildGoodDetailKey(id);
        GoodDetailDTO goodDetailDTO = (GoodDetailDTO) redisUtils.get(key);
        return goodDetailDTO;
    }

    
    public PageResponse<GoodSimpleDTO> getList(int page, int pagesize){
        String key = buildGoodListKey(page, pagesize);
        PageResponse<GoodSimpleDTO> goodListDTO = (PageResponse<GoodSimpleDTO>) redisUtils.get(key);
        return goodListDTO;
    }

    public void deleteDetail(Long id) {
        String key = buildGoodDetailKey(id);
        redisUtils.deleteKey(key);
    }

    public void deleteAllListCache() {
        Set<String> keys = redisUtils.findKeysWithPrefix(GOOD+LIST);
        redisUtils.deleteKeys(keys);
    }

    public void doubleDelete(Long id){
        deleteDetail(id);
        deleteAllListCache();
        DELAY_DELETE_POOL.schedule( () -> {
            try {
                deleteDetail(id);
                deleteAllListCache();
            } catch (Exception e) {
                log.info("延迟双删失败");
            }
        }, DOUBLE_DELETE_TIME, TimeUnit.SECONDS);
    }
}
