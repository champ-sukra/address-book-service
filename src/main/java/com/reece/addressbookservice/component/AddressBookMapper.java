package com.reece.addressbookservice.component;

import com.reece.addressbookservice.dto.AddressBookRequest;
import com.reece.addressbookservice.dto.AddressBookResponse;
import com.reece.addressbookservice.dto.ContactRequest;
import com.reece.addressbookservice.dto.ContactResponse;
import com.reece.addressbookservice.entity.AddressBook;
import com.reece.addressbookservice.entity.Contact;
import com.reece.addressbookservice.entity.PhoneNo;
import org.springframework.stereotype.Component;

@Component
public class AddressBookMapper {
    public AddressBookResponse toAddressBookResponse(AddressBook addressBook) {
        return new AddressBookResponse(
                addressBook.getId(),
                addressBook.getName()
        );
    }

    public AddressBook toAddressBookEntity(AddressBookRequest request) {
        return new AddressBook(null, request.name());
    }
}
