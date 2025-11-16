package com.example.mall.controller.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 商品列表中的粗略信息DTO
 */
@Data
public class GoodSimpleDTO {
    private Long id;

    private String name;

    private String title;

    private BigDecimal price;

    private String imageUrl;

    private String stockStatus;
}