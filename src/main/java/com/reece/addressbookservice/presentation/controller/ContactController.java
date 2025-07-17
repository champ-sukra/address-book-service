package com.reece.addressbookservice.presentation.controller;

import com.reece.addressbookservice.application.mapper.ContactMapper;
import com.reece.addressbookservice.presentation.dto.global.ApiResponse;
import com.reece.addressbookservice.presentation.dto.ContactResponse;
import com.reece.addressbookservice.presentation.dto.PhoneNoRequest;
import com.reece.addressbookservice.domain.entity.Contact;
import com.reece.addressbookservice.domain.entity.PhoneNo;
import com.reece.addressbookservice.application.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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

    @Operation(summary = "Get a contact by ID", description = "Retrieves the details of a contact by its ID.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Contact retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid contact ID"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Contact not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ContactResponse>> getContactById(@PathVariable("id") Long id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        Contact contact = contactService.getContactDetail(id);
        ContactResponse response = contactMapper.toContactResponse(contact);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get unique contacts", description = "Retrieves a list of unique contacts across all address books.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Unique contacts retrieved successfully")
    })
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
    @Operation(summary = "Delete a contact by ID", description = "Deletes a contact by its ID.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Contact deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid contact ID"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Contact not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteContactById(@PathVariable("id") Long id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        contactService.deleteContact(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Phone number added successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid contact ID or phone number"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Contact not found")
    })
    @PatchMapping("/{id}/phone-nos")
    public ResponseEntity<ApiResponse<ContactResponse>> addPhoneNoToContact(
            @PathVariable("id") Long id, @Valid @RequestBody PhoneNoRequest phoneNoRequest) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        Contact contact = contactService.addPhoneNoToContact(id, phoneNoRequest);
        ContactResponse response = contactMapper.toContactResponse(contact);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
