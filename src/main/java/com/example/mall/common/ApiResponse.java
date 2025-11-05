package com.example.mall.common;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.example.mall.common.enums.HttpResultCode;

import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(HttpResultCode rc, T data) {
        return new ApiResponse<>(rc.getHttpCode(), rc.getMessage(), data);
    }

    public static ApiResponse<Void> success(HttpResultCode rc) {
        return new ApiResponse<>(rc.getHttpCode(), rc.getMessage(), null);
    }

    public static <T> ApiResponse<T> error(HttpResultCode rc, T data) {
        return new ApiResponse<>(rc.getHttpCode(), rc.getMessage(), data);
    }

    public static ApiResponse<Void> error(HttpResultCode rc) {
        return new ApiResponse<>(rc.getHttpCode(), rc.getMessage(), null);
    }
}

