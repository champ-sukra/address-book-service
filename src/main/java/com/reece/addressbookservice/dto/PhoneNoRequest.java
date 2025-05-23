package com.reece.addressbookservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PhoneNoRequest(
        @NotBlank
        @Size(min = 8, max = 15, message = "Phone number must be between 8 and 15 characters")
        String phoneNo) {}

