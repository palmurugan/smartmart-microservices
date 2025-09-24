package com.smart.mart.common.config.exception;

/**
 * Exception thrown when there is an error during order calculation.
 */
public class OrderCalculationException extends RuntimeException {
    
    /**
     * Constructs a new OrderCalculationException with the specified detail message.
     *
     * @param message the detail message
     */
    public OrderCalculationException(String message) {
        super(message);
    }

    /**
     * Constructs a new OrderCalculationException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public OrderCalculationException(String message, Throwable cause) {
        super(message, cause);
    }
}
