package com.example.mall.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.mall.common.PageQuery;
import com.example.mall.common.PageResponse;
import com.example.mall.controller.dto.GoodSimpleDTO;
import com.example.mall.mapper.GoodsMapper;
import com.example.mall.service.GoodService;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import com.example.mall.controller.dto.GoodDetailDTO;

@Slf4j
@Service
public class GoodServiceImpl implements GoodService {
    @Resource
    GoodsMapper goodsMapper;

    public PageResponse<GoodSimpleDTO> getGoodsList(PageQuery pageQuery){
        int offset = (pageQuery.getPageNum() - 1) * pageQuery.getPageSize();
        int limit = pageQuery.getPageSize();
        List<GoodSimpleDTO> goods = goodsMapper.getGoodsWithPage(offset, limit);
        Long count = goodsMapper.goodCount();
        PageResponse<GoodSimpleDTO> pageResponse = new PageResponse<GoodSimpleDTO>(goods, count, limit, offset, limit);
        return pageResponse;
    }
    public GoodDetailDTO getGoodDetail(Long id){
        GoodDetailDTO goodDetailDTO = goodsMapper.getGoodById(id);
        return goodDetailDTO;
    }
}
