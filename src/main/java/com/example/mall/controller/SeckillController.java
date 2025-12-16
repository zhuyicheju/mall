package com.example.mall.controller;

import java.util.Set;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mall.common.ApiResponse;
import com.example.mall.common.enums.HttpResultCode;
import com.example.mall.manager.UserRateLimiterManager;
import com.example.mall.service.SeckillService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/seckill")
public class SeckillController {
    @Resource
    UserRateLimiterManager userRateLimiterManager;

    @Resource
    SeckillService seckillService;

    @PostMapping("/preheat/{good_id}")
    public ApiResponse<Void> preheatGood(@PathVariable Long id){
        seckillService.preheatGood(id);
        return ApiResponse.success(HttpResultCode.OK);
    }

    @PostMapping("/preheat/batch")
    public ApiResponse<Void> preheatGoods(@RequestBody Set<Long> ids){
        seckillService.preheatGoods(ids);
        return ApiResponse.success(HttpResultCode.OK);
    }

    @PostMapping("/preheat/seckill_goods")
    public ApiResponse<Void> preheatSeckillGoods(){
        seckillService.preheatAllSeckillGoods();
        return ApiResponse.success(HttpResultCode.OK);
    }

    @PostMapping("/seckill")
    public ApiResponse<String> seckill(Long userId, Long goodId) {

        if (!userRateLimiterManager.tryAcquire(userId)) {
            return ApiResponse.error(HttpResultCode.BAD_REQUEST, "请求过于频繁，请稍后再试");
        }

        // ↓ 只有通过限流，才能进入真正的业务
        seckillService.doSeckill(userId, goodId);
        return ApiResponse.success(HttpResultCode.OK, "秒杀成功");
    }

}
