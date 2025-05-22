package com.reece.addressbookservice.dto;

import com.reece.addressbookservice.entity.PhoneNumber;

import java.util.Set;

public record ContactResponse(Long id, String name, Set<PhoneNumber> phoneNumbers) { }