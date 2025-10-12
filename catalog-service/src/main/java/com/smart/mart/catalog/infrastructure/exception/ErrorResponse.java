package com.smart.mart.catalog.infrastructure.exception;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class ErrorResponse {
    private int status;
    private String message;
    private ZonedDateTime timestamp;

    public ErrorResponse(int status, String message, ZonedDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }
}
