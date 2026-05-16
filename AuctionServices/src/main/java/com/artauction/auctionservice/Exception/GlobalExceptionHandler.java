package com.artauction.auctionservice.Exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // ─────────────────────────────────────────────
    // 400 - @Valid failed on @RequestBody DTO
    // ─────────────────────────────────────────────
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // ─────────────────────────────────────────────
    // 400 - @Valid constraint violations on @RequestParam
    // ─────────────────────────────────────────────
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException e) {
        Map<String, String> errors = new HashMap<>();
        e.getConstraintViolations().forEach(violation -> {
            String field = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.put(field, message);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // ─────────────────────────────────────────────
    // 400 - @RequestParam type mismatch
    // ─────────────────────────────────────────────
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Invalid value for parameter '" + e.getName() + "': expected a valid number.");
    }

    // ─────────────────────────────────────────────
    // 400 - Missing required @RequestParam
    // ─────────────────────────────────────────────
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParam(MissingServletRequestParameterException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Missing required parameter: '" + e.getParameterName() + "'.");
    }

    // ─────────────────────────────────────────────
    // 400 - Unreadable or malformed JSON body
    // ─────────────────────────────────────────────
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleUnreadableMessage(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Malformed or unreadable request body.");
    }

    // ─────────────────────────────────────────────
    // 404 - No route matched the request
    // ─────────────────────────────────────────────
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNoHandlerFound(NoHandlerFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Route not found: " + e.getRequestURL());
    }

    // ─────────────────────────────────────────────
    // 408 - Kafka request/reply timed out
    // ─────────────────────────────────────────────
    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<String> handleTimeout(TimeoutException e) {
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                .body("Request timed out while waiting for a response from another service.");
    }

    // ─────────────────────────────────────────────
    // 408 - Kafka CompletableFuture thread interrupted
    // ─────────────────────────────────────────────
    @ExceptionHandler(InterruptedException.class)
    public ResponseEntity<String> handleInterrupted(InterruptedException e) {
        Thread.currentThread().interrupt();
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                .body("Service communication was interrupted. Please try again.");
    }

    // ─────────────────────────────────────────────
    // 500 - Unexpected null value
    // ─────────────────────────────────────────────
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointer(NullPointerException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected null value was encountered. Please contact support.");
    }

    // ─────────────────────────────────────────────
    // 500 - Any other unhandled exception
    // ─────────────────────────────────────────────
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneral(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + e.getMessage());
    }
}
