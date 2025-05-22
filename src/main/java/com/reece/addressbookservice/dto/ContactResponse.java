package com.reece.addressbookservice.dto;

import com.reece.addressbookservice.entity.PhoneNo;

import java.util.Set;

public record ContactResponse(Long id, String name, Set<PhoneNo> phoneNos) { }