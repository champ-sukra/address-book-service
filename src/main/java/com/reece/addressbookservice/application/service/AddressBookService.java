package com.reece.addressbookservice.application.service;

import com.reece.addressbookservice.presentation.dto.AddressBookRequest;
import com.reece.addressbookservice.presentation.dto.ContactRequest;
import com.reece.addressbookservice.domain.entity.AddressBook;
import com.reece.addressbookservice.domain.entity.Contact;

import java.util.List;
import java.util.Set;

public interface AddressBookService {
    AddressBook createAddressBook(AddressBookRequest request);
    List<AddressBook> getAddressBooks();
    void deleteAddressBookById(int addressBookId);
    Contact createContact(int addressBookId, ContactRequest contactRequest);
    Set<Contact> getContactsByAddressId(int addressBookId);
}
