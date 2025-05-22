package com.reece.addressbookservice.service;

import com.reece.addressbookservice.dto.ContactRequest;
import com.reece.addressbookservice.dto.ContactResponse;
import com.reece.addressbookservice.entity.AddressBook;
import com.reece.addressbookservice.entity.Contact;
import com.reece.addressbookservice.exception.DataNotFoundException;
import com.reece.addressbookservice.repository.AddressBookRepository;
import com.reece.addressbookservice.repository.ContactRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {

    private final AddressBookRepository addressBookRepository;
    private final ContactService contactService;

    public AddressBookServiceImpl(AddressBookRepository addressBookRepository, ContactService contactService) {
        this.addressBookRepository = addressBookRepository;
        this.contactService = contactService;
    }

    @Transactional
    @Override
    public Contact createContact(Long addressBookId, ContactRequest contactRequest) {
        AddressBook addressBook = addressBookRepository.findById(addressBookId)
                .orElseThrow(() -> new DataNotFoundException("Address book not found"));

        //create abd persist contact via service layer
        Contact contact = contactService.createContact(contactRequest);

        //add contact to address book
        addressBook.getContacts().add(contact);
        addressBookRepository.save(addressBook);

        return contact;
    }

    ContactResponse transformToContactResponse(Contact contact) {
        return new ContactResponse(contact.getId(),
                contact.getName(),
                contact.getPhoneNumbers()
        );
    }
}
