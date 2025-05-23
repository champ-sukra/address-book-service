package com.reece.addressbookservice.service;

import com.reece.addressbookservice.dto.AddressBookRequest;
import com.reece.addressbookservice.dto.ContactRequest;
import com.reece.addressbookservice.entity.AddressBook;
import com.reece.addressbookservice.entity.Contact;

import java.util.List;
import java.util.Set;

public interface AddressBookService {
    AddressBook createAddressBook(AddressBookRequest request);
    List<AddressBook> getAddressBooks();
    void deleteAddressBookById(int id);
    Contact createContact(int addressBookId, ContactRequest contactRequest);
    Set<Contact> getContactsByAddressId(int addressBookId);
}
