package com.reece.addressbookservice.service;

import com.reece.addressbookservice.component.AddressBookMapper;
import com.reece.addressbookservice.dto.AddressBookRequest;
import com.reece.addressbookservice.dto.ContactRequest;
import com.reece.addressbookservice.entity.AddressBook;
import com.reece.addressbookservice.entity.Contact;
import com.reece.addressbookservice.exception.DataNotFoundException;
import com.reece.addressbookservice.repository.AddressBookRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AddressBookServiceImpl implements AddressBookService {

    private final AddressBookRepository addressBookRepository;
    private final ContactService contactService;
    private final AddressBookMapper addressBookMapper;

    public AddressBookServiceImpl(AddressBookRepository addressBookRepository, ContactService contactService, AddressBookMapper addressBookMapper) {
        this.addressBookRepository = addressBookRepository;
        this.contactService = contactService;
        this.addressBookMapper = addressBookMapper;
    }

    @Override
    public AddressBook createAddressBook(AddressBookRequest request) {
        return addressBookRepository.findByName(request.name())
                .orElseGet(() -> addressBookRepository.save(addressBookMapper.toAddressBookEntity(request)));
    }

    @Override
    public List<AddressBook> getAddressBooks() {
        return addressBookRepository.findAll();
    }

    @Override
    public void deleteAddressBookById(int id) {
        AddressBook addressBook = addressBookRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Address book not found"));
        addressBookRepository.delete(addressBook);
    }

    @Transactional
    @Override
    public Contact createContact(int addressBookId, ContactRequest contactRequest) {
        AddressBook addressBook = addressBookRepository.findById(addressBookId)
                .orElseThrow(() -> new DataNotFoundException("Address book not found"));

        //create persist contact via service layer
        Contact contact = contactService.createContact(contactRequest);

        //add contact to address book
        addressBook.getContacts().add(contact);
        addressBookRepository.save(addressBook);

        return contact;
    }

    @Override
    public Set<Contact> getContactsByAddressId(int addressBookId) {
        AddressBook addressBook = addressBookRepository.findById(addressBookId)
                .orElseThrow(() -> new DataNotFoundException("Address book not found"));
        return addressBook.getContacts();
    }
}
