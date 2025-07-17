package com.reece.addressbookservice.domain.service;

import com.reece.addressbookservice.application.mapper.ContactMapper;
import com.reece.addressbookservice.application.mapper.PhoneNoMapper;
import com.reece.addressbookservice.application.ContactService;
import com.reece.addressbookservice.domain.entity.AddressBook;
import com.reece.addressbookservice.domain.entity.Contact;
import com.reece.addressbookservice.domain.entity.PhoneNo;
import com.reece.addressbookservice.presentation.dto.ContactRequest;
import com.reece.addressbookservice.presentation.dto.PhoneNoRequest;
import com.reece.addressbookservice.presentation.exception.DataNotFoundException;
import com.reece.addressbookservice.infrastructure.persistance.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;

    private final ContactMapper contactMapper;

    private final PhoneNoMapper phoneNoMapper;

    public ContactServiceImpl(ContactRepository contactRepository, ContactMapper contactMapper, PhoneNoMapper phoneNoMapper) {
        this.contactRepository = contactRepository;
        this.contactMapper = contactMapper;
        this.phoneNoMapper = phoneNoMapper;
    }

    @Override
    public Contact createContact(ContactRequest contactRequest) {
        Contact contact = contactMapper.toContactEntity(contactRequest);
        return contactRepository.save(contact);
    }

    @Override
    public Contact getContactDetail(Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("contact_not_found", String.format("Contact - %d is not found", id)));
    }

    @Override
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    @Override
    public void deleteContact(Long id) {
        Contact contact = getContactDetail(id);

        for (AddressBook addressBook : contact.getAddressBooks()) {
            addressBook.getContacts().remove(contact);
        }

        contactRepository.delete(contact);
    }

    @Override
    public Contact addPhoneNoToContact(Long contactId, PhoneNoRequest request) {
        Contact contact = getContactDetail(contactId);

        PhoneNo phoneNo = phoneNoMapper.toPhoneNoEntity(request);
        contact.addPhoneNo(phoneNo);

        return contactRepository.save(contact);
    }
}
