package com.reece.addressbookservice.controller;

import com.reece.addressbookservice.dto.global.ApiResponse;
import com.reece.addressbookservice.mapper.AddressBookMapper;
import com.reece.addressbookservice.mapper.ContactMapper;
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
    public ResponseEntity<ApiResponse<AddressBookResponse>> createAddressBook(@RequestBody @Valid AddressBookRequest addressBookRequest) {
        AddressBook addressBook = addressBookService.createAddressBook(addressBookRequest);
        AddressBookResponse response = addressBookMapper.toAddressBookResponse(addressBook);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressBookResponse>>> getAddressBooks() {
        List<AddressBook> addressBooks = addressBookService.getAddressBooks();
        List<AddressBookResponse> addressBookResponses = addressBooks.stream()
                .map(addressBookMapper::toAddressBookResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(addressBookResponses));
    }

    //IDEMPOTENT DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAddressBook(@PathVariable("id") Integer id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        addressBookService.deleteAddressBookById(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/{id}/contacts")
    public ResponseEntity<ApiResponse<ContactResponse>> createContact(@PathVariable("id") Integer id,
                                                         @RequestBody ContactRequest contactRequest) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        Contact contact = addressBookService.createContact(id, contactRequest);
        ContactResponse response = contactMapper.toContactResponse(contact);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

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


