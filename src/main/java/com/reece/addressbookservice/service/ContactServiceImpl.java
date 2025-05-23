package com.reece.addressbookservice.service;

import com.reece.addressbookservice.component.ContactMapper;
import com.reece.addressbookservice.component.PhoneNoMapper;
import com.reece.addressbookservice.dto.ContactRequest;
import com.reece.addressbookservice.dto.PhoneNoRequest;
import com.reece.addressbookservice.entity.AddressBook;
import com.reece.addressbookservice.entity.Contact;
import com.reece.addressbookservice.entity.PhoneNo;
import com.reece.addressbookservice.exception.DataNotFoundException;
import com.reece.addressbookservice.repository.ContactRepository;
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
                .orElseThrow(() -> new DataNotFoundException("Contact not found"));
    }

    @Override
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    @Override
    public void deleteContact(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("not_found"));

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
