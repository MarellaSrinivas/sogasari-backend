package com.sogasari.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {



    // ==========================================
    // Customer not found excemption handler
    // ==========================================
    @ExceptionHandler(CustomerNotFoundException.class)
public ResponseEntity<Map<String, Object>> handleCustomerNotFound(
        CustomerNotFoundException exception,
        HttpServletRequest request
) {

    return buildResponse(
            HttpStatus.NOT_FOUND,
            exception.getMessage(),
            request.getRequestURI()
    );
}

    // ==========================================
    // GENERAL BUSINESS ERRORS
    // ==========================================

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
            RuntimeException exception,
            HttpServletRequest request
    ) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                request.getRequestURI()
        );
    }


    // ==========================================
    // VALIDATION ERRORS
    // ==========================================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {

        Map<String, String> errors =
                new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "timestamp",
                LocalDateTime.now()
        );

        response.put(
                "status",
                HttpStatus.BAD_REQUEST.value()
        );

        response.put(
                "message",
                "Validation failed"
        );

        response.put(
                "errors",
                errors
        );

        response.put(
                "path",
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


    // ==========================================
    // FALLBACK
    // ==========================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(
            Exception exception,
            HttpServletRequest request
    ) {

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Something went wrong. Please try again.",
                request.getRequestURI()
        );
    }


    // ==========================================
    // RESPONSE BUILDER
    // ==========================================

    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status,
            String message,
            String path
    ) {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "timestamp",
                LocalDateTime.now()
        );

        response.put(
                "status",
                status.value()
        );

        response.put(
                "message",
                message
        );

        response.put(
                "path",
                path
        );

        return ResponseEntity
                .status(status)
                .body(response);
    }
}