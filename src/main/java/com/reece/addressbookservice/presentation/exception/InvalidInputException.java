package com.reece.addressbookservice.presentation.exception;

public class InvalidInputException extends RuntimeException {
    private final String errorCode;
    private final String errorMessage;

    public InvalidInputException(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}