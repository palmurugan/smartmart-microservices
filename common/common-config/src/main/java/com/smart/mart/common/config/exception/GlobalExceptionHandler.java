package com.smart.mart.common.config.exception;

import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(OrderNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleOrderNotFoundException(OrderNotFoundException ex, WebRequest request) {
    ErrorResponse errorResponse = new ErrorResponse(
        LocalDateTime.now(),
        HttpStatus.NOT_FOUND.value(),
        HttpStatus.NOT_FOUND.getReasonPhrase(),
        ex.getMessage(),
        request.getDescription(false)
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex,
      WebRequest request) {
    log.error("Resource not found: {}", ex.getMessage());
    ErrorResponse errorResponse = createErrorResponse(
        ex.getStatus(),
        ex.getErrorCode(),
        ex.getMessage(),
        request.getDescription(false)
    );
    return new ResponseEntity<>(errorResponse, ex.getStatus());
  }

  @ExceptionHandler(ResourceAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleResourceAlreadyExistsException(
      ResourceAlreadyExistsException ex, WebRequest request) {
    log.error("Resource already exists: {}", ex.getMessage());
    ErrorResponse errorResponse = createErrorResponse(
        HttpStatus.CONFLICT,
        "Conflict",
        ex.getMessage(),
        request.getDescription(false)
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex,
      WebRequest request) {
    log.error("Validation error: {}", ex.getMessage());
    ErrorResponse errorResponse = createErrorResponse(
        ex.getStatus(),
        ex.getErrorCode(),
        ex.getMessage(),
        request.getDescription(false)
    );
    return new ResponseEntity<>(errorResponse, ex.getStatus());
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpHeaders headers,
      HttpStatusCode status, WebRequest request) {

    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    String errorMessage = "Validation failed for fields: " +
        errors.entrySet().stream()
            .map(e -> e.getKey() + " - " + e.getValue())
            .collect(Collectors.joining(", "));

    log.error("Method argument not valid: {}", errorMessage);

    ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .errorCode("VALIDATION_FAILED")
        .message(errorMessage)
        .details(request.getDescription(false))
        .validationErrors(errors)
        .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(
      ConstraintViolationException ex, WebRequest request) {

    log.error("Constraint violation: {}", ex.getMessage());

    ErrorResponse errorResponse = createErrorResponse(
        HttpStatus.BAD_REQUEST,
        "CONSTRAINT_VIOLATION",
        "Constraint violation: " + ex.getMessage(),
        request.getDescription(false)
    );

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex, HttpHeaders headers,
      HttpStatusCode status, WebRequest request) {

    log.error("Malformed JSON request: {}", ex.getMessage());

    ErrorResponse errorResponse = createErrorResponse(
        HttpStatus.BAD_REQUEST,
        "MALFORMED_JSON",
        "Malformed JSON request: " + ex.getMostSpecificCause().getMessage(),
        request.getDescription(false)
    );

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleAllUncaughtException(
      Exception ex, WebRequest request) {

    log.error("Unexpected error occurred: {}", ex.getMessage(), ex);

    ErrorResponse errorResponse = createErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "INTERNAL_SERVER_ERROR",
        "An unexpected error occurred: " + ex.getMessage(),
        request.getDescription(false)
    );

    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private ErrorResponse createErrorResponse(
      HttpStatus status, String errorCode, String message, String details) {

    return ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(status.value())
        .error(status.getReasonPhrase())
        .errorCode(errorCode)
        .message(message)
        .details(details)
        .build();
  }
}
