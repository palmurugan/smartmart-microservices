package com.smart.mart.common.config.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseException {
    
    private static final String ERROR_CODE = "RESOURCE_NOT_FOUND";
    
    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, ERROR_CODE, message);
    }
    
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(HttpStatus.NOT_FOUND, ERROR_CODE, 
              String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(HttpStatus.NOT_FOUND, ERROR_CODE, message, cause);
    }
}
