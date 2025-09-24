package com.smart.mart.common.config.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends BaseException {
    
    private static final String ERROR_CODE = "VALIDATION_ERROR";
    
    public ValidationException(String message) {
        super(HttpStatus.BAD_REQUEST, ERROR_CODE, message);
    }
    
    public ValidationException(String message, Throwable cause) {
        super(HttpStatus.BAD_REQUEST, ERROR_CODE, message, cause);
    }
    
    public ValidationException(String field, String error) {
        super(HttpStatus.BAD_REQUEST, ERROR_CODE, String.format("Validation error for field '%s': %s", field, error));
    }
}
