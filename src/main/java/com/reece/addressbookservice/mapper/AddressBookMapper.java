package com.reece.addressbookservice.mapper;

import com.reece.addressbookservice.dto.AddressBookRequest;
import com.reece.addressbookservice.dto.AddressBookResponse;
import com.reece.addressbookservice.entity.AddressBook;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressBookMapper {
    AddressBookResponse toAddressBookResponse(AddressBook addressBook);

    AddressBook toAddressBookEntity(AddressBookRequest request);
}