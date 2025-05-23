package com.reece.addressbookservice.exception;

public class DataNotFoundException extends RuntimeException {
    private final String errorCode;
    private final String errorMessage;

    public DataNotFoundException(String errorCode, String errorMessage) {
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