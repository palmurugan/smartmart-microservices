package com.smart.mart.catalog.infrastructure.exception;

import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.Map;

@Getter
public class ValidationErrorResponse extends ErrorResponse {
    private Map<String, String> errors;

    public ValidationErrorResponse(int status, String message, Map<String, String> errors, ZonedDateTime timestamp) {
        super(status, message, timestamp);
        this.errors = errors;
    }
}
