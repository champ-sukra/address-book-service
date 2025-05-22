package com.reece.addressbookservice.controller;

import com.reece.addressbookservice.component.ContactMapper;
import com.reece.addressbookservice.dto.ContactResponse;
import com.reece.addressbookservice.entity.Contact;
import com.reece.addressbookservice.service.ContactService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    private final ContactService contactService;

    private final ContactMapper contactMapper;

    public ContactController(ContactService contactService, ContactMapper contactMapper) {
        this.contactService = contactService;
        this.contactMapper = contactMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactResponse> getContactById(@PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Contact contact = contactService.getContactDetail(id);;
        return new ResponseEntity<>(contactMapper.toContactResponse(contact), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ContactResponse>> getAllContacts() {
        List<Contact> contacts = contactService.getAllContacts();
        return new ResponseEntity<>(contacts.stream().map(contactMapper::toContactResponse).toList(), HttpStatus.OK);
    }
}
