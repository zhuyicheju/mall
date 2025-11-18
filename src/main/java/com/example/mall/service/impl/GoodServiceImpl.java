package com.example.mall.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.mall.common.PageQuery;
import com.example.mall.common.PageResponse;
import com.example.mall.controller.dto.GoodSimpleDTO;
import com.example.mall.mapper.GoodsMapper;
import com.example.mall.service.GoodService;
import com.example.mall.service.Utils.GoodRedisUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import com.example.mall.controller.dto.GoodDetailDTO;

@Slf4j
@Service
public class GoodServiceImpl implements GoodService {

    @Resource
    GoodsMapper goodsMapper;

    @Resource 
    GoodRedisUtils goodRedisUtils;

    private final static String REDIS_NULL = "NULL";

    public PageResponse<GoodSimpleDTO> getGoodsList(PageQuery pageQuery){
        int page = pageQuery.getPageNum();
        int pagesize = pageQuery.getPageSize();
        int offset = (pageQuery.getPageNum() - 1) * pageQuery.getPageSize();
        int limit = pageQuery.getPageSize();

        Object cacheValue = goodRedisUtils.getList(page, pagesize);
        if (cacheValue != null){
            if (REDIS_NULL.equals(cacheValue)){
                return null;
            }
            return (PageResponse<GoodSimpleDTO>) cacheValue;
        }
        
        List<GoodSimpleDTO> goods = goodsMapper.getGoodsWithPage(offset, limit);
        Long count = goodsMapper.goodCount();
        PageResponse<GoodSimpleDTO> pageResponse = new PageResponse<GoodSimpleDTO>(goods, count, limit, offset, limit);
        
        goodRedisUtils.setList(page, pagesize, pageResponse);
        
        return pageResponse;
    }
    public GoodDetailDTO getGoodDetail(Long id){
        Object cacheValue = goodRedisUtils.getDetail(id);

        if (cacheValue != null){
            if(REDIS_NULL.equals(cacheValue)){
                return null;
            }
            return (GoodDetailDTO) cacheValue;
        }
        GoodDetailDTO goodDetailDTO = goodsMapper.getGoodById(id);
        goodRedisUtils.setDetail(id, goodDetailDTO);

        return goodDetailDTO;
    }
}
