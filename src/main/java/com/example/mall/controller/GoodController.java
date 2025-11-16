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

@RestController
@RequestMapping("/good")
public class GoodController {

    @Resource
    GoodService goodService;

    @GetMapping("list")
    public ApiResponse<PageResponse<GoodSimpleDTO>> getGoodsList(PageQuery pageQuery){
        return ApiResponse.success(HttpResultCode.OK, goodService.getGoodsList(pageQuery));
    }

    @GetMapping("/{id}/detail")
    public ApiResponse<GoodDetailDTO> getGoodDetail(@PathVariable Long id){
        return ApiResponse.success(HttpResultCode.OK, goodService.getGoodDetail(id));
    }

}
