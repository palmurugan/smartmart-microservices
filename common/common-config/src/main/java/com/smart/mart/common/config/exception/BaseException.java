package com.smart.mart.common.config.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BaseException extends RuntimeException {
    
    private final HttpStatus status;
    private final String errorCode;
    private final String errorMessage;
    
    protected BaseException(HttpStatus status, String errorCode, String errorMessage) {
        super(errorMessage);
        this.status = status;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    
    protected BaseException(HttpStatus status, String errorCode, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.status = status;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
