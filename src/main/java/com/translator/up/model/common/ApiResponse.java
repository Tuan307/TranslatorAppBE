package com.translator.up.model.common;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;
    private String errorCode;
    private String timestamp;

    public ApiResponse(String status, String message, T data, String errorCode, String timestamp) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.errorCode = errorCode;
        this.timestamp = ZonedDateTime.now(ZoneOffset.UTC).toString();
    }
}
