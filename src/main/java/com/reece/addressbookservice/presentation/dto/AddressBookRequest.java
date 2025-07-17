package com.reece.addressbookservice.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressBookRequest(
        @NotBlank(message = "Name must not be empty")
        String name
) {}

