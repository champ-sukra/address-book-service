package com.reece.addressbookservice.application.mapper;

import com.reece.addressbookservice.domain.entity.PhoneNo;
import com.reece.addressbookservice.presentation.dto.PhoneNoRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PhoneNoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contact", ignore = true)
    @Mapping(target = "number", source = "phoneNo")
    PhoneNo toPhoneNoEntity(
            PhoneNoRequest request);
}
