package com.example.mall.service.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SeckillConfigAndStockDTO {
    private Long stock; 
    private LocalDateTime seckillStartTime; 
    private LocalDateTime seckillEndTime;
    private Integer seckillLimit;
    private Integer isSeckill;
}
