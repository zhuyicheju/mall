package com.example.mall.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageResponse<T>{
    private List<T> data;
    private Long total;
    private int totalPages;
    private int pagesize;
    private int currentPage;
}
