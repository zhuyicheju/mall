package com.example.mall.service;

import com.example.mall.common.PageQuery;
import com.example.mall.common.PageResponse;
import com.example.mall.controller.dto.GoodDetailDTO;
import com.example.mall.controller.dto.GoodSimpleDTO;

public interface GoodService {
    public PageResponse<GoodSimpleDTO> getGoodsList(PageQuery pageQuery);
    public GoodDetailDTO getGoodDetail(Long id);
}
