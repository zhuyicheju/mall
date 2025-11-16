package com.example.mall.common.converter;

import com.example.mall.controller.dto.GoodSimpleDTO;
import com.example.mall.entity.Good;

public class GoodConverter {
    public static GoodSimpleDTO GoodToSimpleDto(Good good) {
        GoodSimpleDTO dto = new GoodSimpleDTO();
        dto.setId(good.getId());
        dto.setName(good.getName());
        dto.setTitle(good.getTitle());
        dto.setPrice(good.getPrice());
        dto.setImageUrl(good.getImageUrl());
        dto.setStockStatus(good.getStock() > 0 ? "有货" : "无货");
        return dto;
    }
}
