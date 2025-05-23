package com.reece.addressbookservice.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ContactRequest(
        @NotBlank(message = "Name must not be empty")
        String name, List<String> phoneNos) { }

