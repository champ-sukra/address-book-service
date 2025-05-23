package com.reece.addressbookservice.controller;

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
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        addressBookService.deleteAddressBookById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/contacts")
    public ResponseEntity<ContactResponse> createContact(@PathVariable("id") Integer id,
                                                         @RequestBody ContactRequest contactRequest) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        Contact contact = addressBookService.createContact(id, contactRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contactMapper.toContactResponse(contact));
    }

    @GetMapping("/{id}/contacts")
    public ResponseEntity<Set<ContactResponse>> getContacts(@PathVariable("id") Integer id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().build();
        }

        Set<Contact> contacts = addressBookService.getContactsByAddressId(id);
        Set<ContactResponse> contactResponses = contacts.stream()
                .map(contactMapper::toContactResponse)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(contactResponses);
    }
}


