package com.example.mall.common;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PageQuery {
    @NotNull
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNum = 1;

    @NotNull
    @Min(value = 1, message = "每页条数不能小于1")
    @Max(value = 100, message = "每页条数不能超过100")
    private Integer pageSize = 10;
}