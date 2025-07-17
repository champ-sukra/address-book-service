package com.reece.addressbookservice.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PhoneNoRequest(
        @NotBlank(message = "Phone number cannot be blank")
        @Size(min = 8, max = 15, message = "Phone number must be between 8 and 15 characters")
        @Pattern(regexp = "\\+?[0-9\\s\\-\\(\\)]{8,15}",
                message = "Phone number contains invalid characters")
        String phoneNo
) {}
