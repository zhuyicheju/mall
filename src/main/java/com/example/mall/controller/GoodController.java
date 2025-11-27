package com.example.mall.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.mall.common.ApiResponse;
import com.example.mall.common.PageQuery;
import com.example.mall.common.PageResponse;
import com.example.mall.common.enums.HttpResultCode;
import com.example.mall.controller.dto.GoodDetailDTO;
import com.example.mall.controller.dto.GoodSimpleDTO;
import com.example.mall.service.GoodService;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/good")
public class GoodController {

    @Resource
    GoodService goodService;

    @GetMapping("/list")
    public ApiResponse<PageResponse<GoodSimpleDTO>> getGoodsList(PageQuery pageQuery){
        log.info("getGoodsList - 开始: pageQuery={}", pageQuery);
        PageResponse<GoodSimpleDTO> result = goodService.getGoodsList(pageQuery);
        log.info("getGoodsList - 结束: 结果={}", result);
        return ApiResponse.success(HttpResultCode.OK, result);
    }

    @GetMapping("/{id}/detail")
    public ApiResponse<GoodDetailDTO> getGoodDetail(@PathVariable Long id){
        log.info("getGoodDetail - 开始: id={}", id);
        GoodDetailDTO result = goodService.getGoodDetail(id);
        log.info("getGoodDetail - 结束: id={}, 结果={}", id, result);
        return ApiResponse.success(HttpResultCode.OK, result);
    }

}