package com.reece.addressbookservice.dto.global;

public class ApiResponse<T> {
    private String code;
    private T data;

    public ApiResponse(String code, T data) {
        this.code = code;
        this.data = data;
    }
}

