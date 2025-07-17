package com.reece.addressbookservice.presentation.exception.controller;

import com.reece.addressbookservice.presentation.dto.global.ApiResponse;
import com.reece.addressbookservice.presentation.exception.DataNotFoundException;
import com.reece.addressbookservice.presentation.exception.InvalidInputException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAll(Exception ex, HttpServletRequest request) {
        logger.error("Invalid request: {}", request);

        String code = "invalid_request";
        String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(code, null, message));
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataNotFound(DataNotFoundException ex) {
        logger.error("Data not found: {}", ex.getErrorMessage(), ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(ex.getErrorCode(), null, ex.getErrorMessage()));
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidInput(InvalidInputException ex) {
        logger.error("Invalid input: {}", ex.getErrorMessage(), ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(ex.getErrorCode(), null, ex.getErrorMessage()));
    }
}
