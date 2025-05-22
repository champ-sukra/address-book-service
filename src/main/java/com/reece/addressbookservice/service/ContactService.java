package com.reece.addressbookservice.service;

import com.reece.addressbookservice.dto.ContactRequest;
import com.reece.addressbookservice.dto.ContactResponse;
import com.reece.addressbookservice.entity.Contact;

import java.util.List;
import java.util.Set;

public interface ContactService {
    Contact createContact(ContactRequest contactRequest);
    Contact getContactDetail(Long id);
    List<Contact> getAllContacts();
}
