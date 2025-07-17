package com.reece.addressbookservice.presentation.dto;

import com.reece.addressbookservice.domain.entity.PhoneNo;

import java.util.Set;

public record ContactResponse(Long id, String name, Set<PhoneNo> phoneNos) { }