package com.reece.addressbookservice.application.mapper;

import com.reece.addressbookservice.presentation.dto.AddressBookRequest;
import com.reece.addressbookservice.presentation.dto.AddressBookResponse;
import com.reece.addressbookservice.domain.entity.AddressBook;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressBookMapper {
    default AddressBookResponse toAddressBookResponse(AddressBook addressBook) {
        return new AddressBookResponse(addressBook.getId(), addressBook.getName());
    }

    default AddressBook toAddressBookEntity(AddressBookRequest request) {
        return new AddressBook(request.name());
    }
}