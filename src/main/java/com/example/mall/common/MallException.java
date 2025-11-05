package com.example.mall.common;

import com.example.mall.common.enums.HttpResultCode;

/**
 * 项目统一业务异常类
 */
public class MallException extends RuntimeException {
    
    private final HttpResultCode resultCode;

    public MallException(HttpResultCode resultCode) {
        super(resultCode.getMessage()); 
        this.resultCode = resultCode;
    }


    public MallException(HttpResultCode resultCode, String message) {
        super(message);
        this.resultCode = resultCode;
    }

    public HttpResultCode getResultCode() {
        return resultCode;
    }

    public int getHttpCode() {
        return resultCode.getHttpCode();
    }
}