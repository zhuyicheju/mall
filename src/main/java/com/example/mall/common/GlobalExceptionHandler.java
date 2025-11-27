package com.example.mall.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MallException.class)
    public ApiResponse<Void> handleMallException(MallException e) {
        return ApiResponse.error(e.getResultCode());
    }
}