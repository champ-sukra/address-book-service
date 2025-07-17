package com.reece.addressbookservice.presentation.controller;

import com.reece.addressbookservice.application.mapper.ContactMapper;
import com.reece.addressbookservice.presentation.dto.global.ApiResponse;
import com.reece.addressbookservice.application.mapper.AddressBookMapper;
import com.reece.addressbookservice.presentation.dto.AddressBookRequest;
import com.reece.addressbookservice.presentation.dto.AddressBookResponse;
import com.reece.addressbookservice.presentation.dto.ContactRequest;
import com.reece.addressbookservice.presentation.dto.ContactResponse;
import com.reece.addressbookservice.domain.entity.AddressBook;
import com.reece.addressbookservice.domain.entity.Contact;
import com.reece.addressbookservice.application.AddressBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Create a new address book", description = "Creates a new address book with the given name.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Address book created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<AddressBookResponse>> createAddressBook(@RequestBody @Valid AddressBookRequest addressBookRequest) {
        AddressBook addressBook = addressBookService.createAddressBook(addressBookRequest);
        AddressBookResponse response = addressBookMapper.toAddressBookResponse(addressBook);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    @Operation(summary = "Create a new address book", description = "Creates a new address book with the given name.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Address book created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressBookResponse>>> getAddressBooks() {
        List<AddressBook> addressBooks = addressBookService.getAddressBooks();
        List<AddressBookResponse> addressBookResponses = addressBooks.stream()
                .map(addressBookMapper::toAddressBookResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(addressBookResponses));
    }

    //IDEMPOTENT DELETE
    @Operation(summary = "Delete an address book", description = "Deletes an address book by its ID.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Address book deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Address book not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAddressBook(@PathVariable("id") Integer id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        addressBookService.deleteAddressBookById(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "Create a new contact", description = "Creates a new contact in the specified address book.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Contact created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data or address book ID"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Address book not found")
    })
    @PostMapping("/{id}/contacts")
    public ResponseEntity<ApiResponse<ContactResponse>> createContact(@PathVariable("id") Integer id,
                                                         @RequestBody @Valid ContactRequest contactRequest) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        Contact contact = addressBookService.createContact(id, contactRequest);
        ContactResponse response = contactMapper.toContactResponse(contact);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @Operation(summary = "Get contacts in an address book", description = "Retrieves all contacts associated with the specified address book.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Contacts retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid address book ID"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Address book not found")
    })
    @GetMapping("/{id}/contacts")
    public ResponseEntity<ApiResponse<Set<ContactResponse>>> getContacts(@PathVariable("id") Integer id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        Set<Contact> contacts = addressBookService.getContactsByAddressId(id);
        Set<ContactResponse> responses = contacts.stream()
                .map(contactMapper::toContactResponse)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
}


