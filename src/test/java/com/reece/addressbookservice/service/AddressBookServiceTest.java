package com.reece.addressbookservice.service;

import com.reece.addressbookservice.dto.ContactRequest;
import com.reece.addressbookservice.entity.AddressBook;
import com.reece.addressbookservice.entity.Contact;
import com.reece.addressbookservice.repository.AddressBookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AddressBookServiceTest {
    @Mock
    AddressBookRepository addressBookRepository;

    @Mock
    ContactService contactService;

    @InjectMocks
    AddressBookServiceImpl addressBookService;

    @Test
    void createContact_shouldReturnContactInAddressBook() {
        //given
        AddressBook addressBook = new AddressBook(1, "WORK");
        ContactRequest request = new ContactRequest("test", List.of("0402465555"));
        Contact expected = new Contact(1L, "test");

        //when
        when(addressBookRepository.findById(any())).thenReturn(java.util.Optional.of(addressBook));
        when(contactService.createContact(any())).thenReturn(expected);

        //then
        Contact result = addressBookService.createContact(1, request);

        assertNotNull(addressBook.getContacts());
        assertTrue(addressBook.getContacts().contains(result));
        assertEquals(expected, result);
        assertNotNull(result);
        assertNotNull(result.getId());
    }

    @Test
    void getContactsByAddressBookId_shouldReturnContacts() {
        //given
        AddressBook addressBook = new AddressBook(1, "WORK");
        Contact contact1 = new Contact(null, "test 1");
        Contact contact2 = new Contact(null, "test 2");
        addressBook.getContacts().addAll(Set.of(contact1, contact2));

        //when
        when(addressBookRepository.findById(any())).thenReturn(java.util.Optional.of(addressBook));

        //then
        var result = addressBookService.getContactsByAddressId(1);

        assertNotNull(result);
        assertTrue(result.contains(contact1));
        assertTrue(result.contains(contact2));
    }
}
