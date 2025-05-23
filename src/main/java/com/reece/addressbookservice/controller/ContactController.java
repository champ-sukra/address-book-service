package com.reece.addressbookservice.controller;

import com.reece.addressbookservice.mapper.ContactMapper;
import com.reece.addressbookservice.dto.ContactResponse;
import com.reece.addressbookservice.dto.PhoneNoRequest;
import com.reece.addressbookservice.entity.Contact;
import com.reece.addressbookservice.entity.PhoneNo;
import com.reece.addressbookservice.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        Contact contact = contactService.getContactDetail(id);;
        return ResponseEntity.ok(contactMapper.toContactResponse(contact));
    }

    @GetMapping("/unique")
    public ResponseEntity<Set<ContactResponse>> getUniqueContacts() {
        Set<String> filtered = new HashSet<>();
        Set<ContactResponse> contactResponses = contactService.getAllContacts().stream()
                .map(contactMapper::toContactResponse)
                .filter(result -> filtered.add(
                        result.name() + "|" +
                                result.phoneNos().stream()
                                        .map(PhoneNo::getNumber)
                                        .sorted()
                                        .collect(Collectors.joining(","))
                ))
                .collect(Collectors.toSet());
        return ResponseEntity.ok(contactResponses);
    }

    //IDEMPOTENT DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContactById(@PathVariable("id") Long id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        contactService.deleteContact(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/phone-nos")
    public ResponseEntity<ContactResponse> addPhoneNoToContact(
            @PathVariable("id") Long id, @Valid @RequestBody PhoneNoRequest phoneNoRequest) {
        Contact contact = contactService.addPhoneNoToContact(id, phoneNoRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contactMapper.toContactResponse(contact));
    }
}
