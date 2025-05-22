package com.reece.addressbookservice.controller;

import com.reece.addressbookservice.component.ContactMapper;
import com.reece.addressbookservice.dto.ContactRequest;
import com.reece.addressbookservice.dto.ContactResponse;
import com.reece.addressbookservice.entity.Contact;
import com.reece.addressbookservice.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ContactResponse> createContact(@PathVariable("id") Long id,
                                                         @RequestBody ContactRequest contactRequest) {
        Contact contact = addressBookService.createContact(id, contactRequest);
        return new ResponseEntity<>(contactMapper.toContactResponse(contact), HttpStatus.CREATED);
    }
}
