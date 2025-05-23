package com.reece.addressbookservice.service;

import com.reece.addressbookservice.dto.ContactRequest;
import com.reece.addressbookservice.dto.PhoneNoRequest;
import com.reece.addressbookservice.entity.Contact;

import java.util.List;

public interface ContactService {
    Contact createContact(ContactRequest contactRequest);
    Contact getContactDetail(Long id);
    List<Contact> getAllContacts();
    void deleteContact(Long id);
    Contact addPhoneNoToContact(Long contactId, PhoneNoRequest request);
}
