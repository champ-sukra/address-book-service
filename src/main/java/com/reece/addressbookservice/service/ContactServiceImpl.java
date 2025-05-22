package com.reece.addressbookservice.service;

import com.reece.addressbookservice.component.ContactMapper;
import com.reece.addressbookservice.dto.ContactRequest;
import com.reece.addressbookservice.dto.ContactResponse;
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
        return contactRepository.save(contactMapper.toContactEntity(contactRequest));
    }

    @Override
    public ContactResponse getContactDetail(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("not_found"));
        return transformToContactResponse(contact);
    }

    @Override
    public List<ContactResponse> getAllContacts() {
        return contactRepository.findAll().stream()
                .map(this::transformToContactResponse)
                .toList();
    }

    ContactResponse transformToContactResponse(Contact contact) {
        return new ContactResponse(contact.getId(),
                contact.getName(),
                contact.getPhoneNumbers()
        );
    }
}
