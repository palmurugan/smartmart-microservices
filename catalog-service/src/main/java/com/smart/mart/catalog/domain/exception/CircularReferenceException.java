package com.smart.mart.catalog.domain.exception;

public class CircularReferenceException extends DomainException {

    public CircularReferenceException(String message) {
        super(message);
    }
}
