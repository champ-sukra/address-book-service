package com.reece.addressbookservice.application.service;

import com.reece.addressbookservice.presentation.dto.ContactRequest;
import com.reece.addressbookservice.presentation.dto.PhoneNoRequest;
import com.reece.addressbookservice.domain.entity.Contact;

import java.util.List;

public interface ContactService {
    Contact createContact(ContactRequest contactRequest);
    Contact getContactDetail(Long id);
    List<Contact> getAllContacts();
    void deleteContact(Long id);
    Contact addPhoneNoToContact(Long contactId, PhoneNoRequest request);
}
