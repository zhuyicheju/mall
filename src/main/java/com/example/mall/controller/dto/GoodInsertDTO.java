package com.example.mall.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class GoodInsertDTO {
    private String name;

    private String title;

    private BigDecimal price;

    private Long stock;

    private String description;

    private String imageUrl;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
