package com.reece.addressbookservice.service;

import com.reece.addressbookservice.dto.ContactRequest;
import com.reece.addressbookservice.dto.ContactResponse;
import com.reece.addressbookservice.entity.Contact;

import java.util.List;

public interface ContactService {
    Contact createContact(ContactRequest contactRequest);
    ContactResponse getContactDetail(Long id);
    List<ContactResponse> getAllContacts();
}
