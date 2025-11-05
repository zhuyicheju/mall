package com.example.mall.common.enums;

public enum HttpResultCode {

    // ======= 2xx: 成功 =======
    OK(200, "OK"),
    CREATED(201, "Created"),

    // ======= 4xx: 客户端错误 =======
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    CONFLICT(409, "Conflict"),
    UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"), // 常用于参数校验失败

    // ======= 5xx: 服务端错误 =======
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable");

    private final int httpCode;
    private final String message;

    HttpResultCode(int httpCode, String message) {
        this.httpCode = httpCode;
        this.message = message;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public String getMessage() {
        return message;
    }
}
