package com.reece.addressbookservice.service;

import com.reece.addressbookservice.mapper.AddressBookMapper;
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
    public void deleteAddressBookById(int addressBookId) {
        AddressBook addressBook = findById(addressBookId);
        addressBookRepository.delete(addressBook);
    }

    @Transactional
    @Override
    public Contact createContact(int addressBookId, ContactRequest contactRequest) {
        AddressBook addressBook = findById(addressBookId);

        //create persist contact via service layer
        Contact contact = contactService.createContact(contactRequest);

        //add contact to address book
        addressBook.getContacts().add(contact);
        addressBookRepository.save(addressBook);

        return contact;
    }

    @Override
    public Set<Contact> getContactsByAddressId(int addressBookId) {
        AddressBook addressBook = findById(addressBookId);
        return addressBook.getContacts();
    }

    private AddressBook findById(int id) {
        return addressBookRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("ab_not_found", String.format("Address book - %d is not found", id)));
    }
}
