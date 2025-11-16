package com.example.mall.controller.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.example.mall.entity.Good;

/**
 * 单个商品的详细信息DTO
 */
@Data
public class GoodDetailDTO {
    private Long id;

    private String name;

    private String title;

    private BigDecimal price;

    private Long stock;

    private String description;

    private String imageUrl;

    private String createTime;
    
    private String updateTime;

    /**
     * 从实体类转换为DTO（包含时间格式化）
     */
    public static GoodDetailDTO fromEntity(Good good) {
        GoodDetailDTO dto = new GoodDetailDTO();
        dto.setId(good.getId());
        dto.setName(good.getName());
        dto.setTitle(good.getTitle());
        dto.setPrice(good.getPrice());
        dto.setStock(good.getStock());
        dto.setDescription(good.getDescription());
        dto.setImageUrl(good.getImageUrl());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        dto.setCreateTime(good.getCreateTime().format(formatter));
        dto.setUpdateTime(good.getUpdateTime().format(formatter));
        return dto;
    }
}