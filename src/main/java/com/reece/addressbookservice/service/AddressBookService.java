package com.reece.addressbookservice.service;

import com.reece.addressbookservice.dto.ContactRequest;
import com.reece.addressbookservice.dto.ContactResponse;
import com.reece.addressbookservice.entity.Contact;

import java.util.List;
import java.util.Set;

public interface AddressBookService {
    Contact createContact(int addressBookId, ContactRequest contactRequest);
    Set<Contact> getContactsByAddressId(int addressBookId);
}
