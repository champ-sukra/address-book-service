package com.reece.addressbookservice.presentation.dto.global;

public class ApiResponse<T> {
    private final String code;
    private final T data;
    private String message;

    public ApiResponse(String code, T data) {
        this.code = code;
        this.data = data;
    }

    public ApiResponse(String code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("success", data);
    }

    public String getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}

