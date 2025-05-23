package com.reece.addressbookservice.mapper;

import com.reece.addressbookservice.dto.PhoneNoRequest;
import com.reece.addressbookservice.entity.PhoneNo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface PhoneNoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contact", ignore = true)
    @Mapping(target = "number", source = "phoneNo")
    PhoneNo toPhoneNoEntity(
            PhoneNoRequest request);
}
