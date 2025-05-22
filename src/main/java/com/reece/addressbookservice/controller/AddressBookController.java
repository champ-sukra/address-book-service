package com.reece.addressbookservice.controller;

import com.reece.addressbookservice.component.ContactMapper;
import com.reece.addressbookservice.dto.ContactRequest;
import com.reece.addressbookservice.dto.ContactResponse;
import com.reece.addressbookservice.entity.Contact;
import com.reece.addressbookservice.service.AddressBookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/address-books")
public class AddressBookController {

    private final AddressBookService addressBookService;

    private final ContactMapper contactMapper;

    public AddressBookController(AddressBookService addressBookService, ContactMapper contactMapper) {
        this.addressBookService = addressBookService;
        this.contactMapper = contactMapper;
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
