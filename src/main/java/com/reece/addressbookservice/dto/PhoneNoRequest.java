package com.reece.addressbookservice.dto;

import jakarta.validation.constraints.NotBlank;

public record PhoneNoRequest(@NotBlank String phoneNo) {}

