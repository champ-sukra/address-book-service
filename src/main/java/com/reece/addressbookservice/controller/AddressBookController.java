package com.reece.addressbookservice.controller;

import com.reece.addressbookservice.component.AddressBookMapper;
import com.reece.addressbookservice.component.ContactMapper;
import com.reece.addressbookservice.dto.AddressBookRequest;
import com.reece.addressbookservice.dto.AddressBookResponse;
import com.reece.addressbookservice.dto.ContactRequest;
import com.reece.addressbookservice.dto.ContactResponse;
import com.reece.addressbookservice.entity.AddressBook;
import com.reece.addressbookservice.entity.Contact;
import com.reece.addressbookservice.service.AddressBookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/address-books")
public class AddressBookController {

    private final AddressBookService addressBookService;

    private final ContactMapper contactMapper;

    private final AddressBookMapper addressBookMapper;

    public AddressBookController(AddressBookService addressBookService,
                                 ContactMapper contactMapper,
                                 AddressBookMapper addressBookMapper) {
        this.addressBookService = addressBookService;
        this.contactMapper = contactMapper;
        this.addressBookMapper = addressBookMapper;
    }

    @PostMapping()
    public ResponseEntity<AddressBookResponse> createAddressBook(@RequestBody @Valid AddressBookRequest addressBookRequest) {
        AddressBook addressBook = addressBookService.createAddressBook(addressBookRequest);
        return new ResponseEntity<>(addressBookMapper.toAddressBookResponse(addressBook), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AddressBookResponse>> getAddressBooks() {
        List<AddressBook> addressBooks = addressBookService.getAddressBooks();
        return new ResponseEntity<>((addressBooks.stream()
                .map(addressBookMapper::toAddressBookResponse).collect(Collectors.toList())), HttpStatus.OK);
    }

    //IDEMPOTENT DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddressBook(@PathVariable("id") Integer id) {
        if (id == null || id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        addressBookService.deleteAddressBookById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/contacts")
    public ResponseEntity<ContactResponse> createContact(@PathVariable("id") Integer id,
                                                         @RequestBody ContactRequest contactRequest) {
        Contact contact = addressBookService.createContact(id, contactRequest);
        return new ResponseEntity<>(contactMapper.toContactResponse(contact), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/contacts")
    public ResponseEntity<Set<ContactResponse>> getContacts(@PathVariable("id") Integer id) {
        Set<Contact> contacts = addressBookService.getContactsByAddressId(id);
        Set<ContactResponse> contactResponses = contacts.stream()
                .map(contactMapper::toContactResponse)
                .collect(Collectors.toSet());
        return new ResponseEntity<>(contactResponses, HttpStatus.OK);
    }
}


