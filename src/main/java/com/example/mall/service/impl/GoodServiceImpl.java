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
        log.info("getGoodsList - 开始: pageQuery={}", pageQuery);
        
        int page = pageQuery.getPageNum();
        int pagesize = pageQuery.getPageSize();
        int offset = (pageQuery.getPageNum() - 1) * pageQuery.getPageSize();
        int limit = pageQuery.getPageSize();
        log.debug("getGoodsList - 分页参数: page={}, pagesize={}, offset={}, limit={}", page, pagesize, offset, limit);

        Object cacheValue = goodRedisUtils.getList(page, pagesize);
        log.debug("getGoodsList - 获取缓存数据: cacheValue={}", cacheValue);
        
        if (cacheValue != null){
            log.debug("getGoodsList - 缓存命中");
            if (REDIS_NULL.equals(cacheValue)){
                log.info("getGoodsList - 缓存值为NULL标识，返回null");
                log.info("getGoodsList - 结束: 返回null");
                return null;
            }
            log.info("getGoodsList - 结束: 返回缓存数据");
            return (PageResponse<GoodSimpleDTO>) cacheValue;
        }
        
        log.debug("getGoodsList - 缓存未命中，查询数据库");
        List<GoodSimpleDTO> goods = goodsMapper.getGoodsWithPage(offset, limit);
        Long count = goodsMapper.goodCount();
        int totalPages = (int) (count + pagesize - 1) / pagesize;
        log.debug("getGoodsList - 数据库查询结果: goods数量={}, 总数量={}, 总页数={}", goods.size(), count, totalPages);
        
        PageResponse<GoodSimpleDTO> pageResponse = new PageResponse<>(
            goods, 
            count, 
            totalPages, 
            pagesize, 
            page
        );
        goodRedisUtils.setList(page, pagesize, pageResponse);
        log.debug("getGoodsList - 缓存数据已设置");

        log.info("getGoodsList - 结束: 返回数据库查询结果");
        return pageResponse;
    }
    
    public GoodDetailDTO getGoodDetail(Long id){
        log.info("getGoodDetail - 开始: id={}", id);
        
        Object cacheValue = goodRedisUtils.getDetail(id);
        log.debug("getGoodDetail - 获取缓存数据: cacheValue={}", cacheValue);

        if (cacheValue != null){
            log.debug("getGoodDetail - 缓存命中");
            if(REDIS_NULL.equals(cacheValue)){
                log.info("getGoodDetail - 缓存值为NULL标识，返回null");
                log.info("getGoodDetail - 结束: 返回null");
                return null;
            }
            log.info("getGoodDetail - 结束: 返回缓存数据");
            return (GoodDetailDTO) cacheValue;
        }
        
        log.debug("getGoodDetail - 缓存未命中，查询数据库");
        GoodDetailDTO goodDetailDTO = goodsMapper.getGoodById(id);
        log.debug("getGoodDetail - 数据库查询结果: goodDetailDTO={}", goodDetailDTO);
        
        goodRedisUtils.setDetail(id, goodDetailDTO);
        log.debug("getGoodDetail - 缓存数据已设置");

        log.info("getGoodDetail - 结束: 返回数据库查询结果");
        return goodDetailDTO;
    }
}