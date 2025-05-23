package com.reece.addressbookservice.controller;

import com.reece.addressbookservice.dto.global.ApiResponse;
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
    public ResponseEntity<ApiResponse<ContactResponse>> getContactById(@PathVariable("id") Long id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        Contact contact = contactService.getContactDetail(id);
        ContactResponse response = contactMapper.toContactResponse(contact);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/unique")
    public ResponseEntity<ApiResponse<Set<ContactResponse>>> getUniqueContacts() {
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
        return ResponseEntity.ok(ApiResponse.success(contactResponses));
    }

    //IDEMPOTENT DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteContactById(@PathVariable("id") Long id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        contactService.deleteContact(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PatchMapping("/{id}/phone-nos")
    public ResponseEntity<ApiResponse<ContactResponse>> addPhoneNoToContact(
            @PathVariable("id") Long id, @Valid @RequestBody PhoneNoRequest phoneNoRequest) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        Contact contact = contactService.addPhoneNoToContact(id, phoneNoRequest);
        ContactResponse response = contactMapper.toContactResponse(contact);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }
}
