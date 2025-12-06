package com.example.mall.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Good {

    private Long id;

    private String name;

    private String title;

    private BigDecimal price;

    private Long stock; 

    private String description;

    private String imageUrl;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private LocalDateTime seckillStartTime; 
    private LocalDateTime seckillEndTime;
    
    private Integer seckillLimit = 1;
    
    private Integer isSeckill = 1;
}