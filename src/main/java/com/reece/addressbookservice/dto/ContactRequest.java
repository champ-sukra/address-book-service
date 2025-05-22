package com.reece.addressbookservice.dto;

import java.util.List;

public record ContactRequest(String name, List<String> phoneNos) { }

