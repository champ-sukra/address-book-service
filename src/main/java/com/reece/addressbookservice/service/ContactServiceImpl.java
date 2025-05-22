package com.reece.addressbookservice.service;

import com.reece.addressbookservice.component.ContactMapper;
import com.reece.addressbookservice.dto.ContactRequest;
import com.reece.addressbookservice.entity.Contact;
import com.reece.addressbookservice.exception.DataNotFoundException;
import com.reece.addressbookservice.repository.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;

    private final ContactMapper contactMapper;

    public ContactServiceImpl(ContactRepository contactRepository, ContactMapper contactMapper) {
        this.contactRepository = contactRepository;
        this.contactMapper = contactMapper;
    }

    @Override
    public Contact createContact(ContactRequest contactRequest) {
        Contact contact = contactMapper.toContactEntity(contactRequest);
        return contactRepository.save(contact);
    }

    @Override
    public Contact getContactDetail(Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("not_found"));
    }

    @Override
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }
}
