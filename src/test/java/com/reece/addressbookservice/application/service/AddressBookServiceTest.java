package com.reece.addressbookservice.application.service;

import com.reece.addressbookservice.application.mapper.AddressBookMapper;
import com.reece.addressbookservice.presentation.dto.AddressBookRequest;
import com.reece.addressbookservice.presentation.dto.ContactRequest;
import com.reece.addressbookservice.domain.entity.AddressBook;
import com.reece.addressbookservice.domain.entity.Contact;
import com.reece.addressbookservice.presentation.exception.DataNotFoundException;
import com.reece.addressbookservice.infrastructure.persistance.AddressBookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
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

    @Mock
    AddressBookMapper addressBookMapper;

    @InjectMocks
    AddressBookServiceImpl addressBookService;

    @Test
    void createAddressBook_shouldReturnAddressBook() {
        //given
        AddressBookRequest request = new AddressBookRequest("WORK");
        AddressBook addressBook = new AddressBook("WORK");
        AddressBook expected = new AddressBook("WORK");

        //when
        when(addressBookRepository.findByName(any())).thenReturn(Optional.empty());
        when(addressBookMapper.toAddressBookEntity(any())).thenReturn(addressBook);
        when(addressBookRepository.save(addressBook)).thenReturn(expected);

        //then
        AddressBook result = addressBookService.createAddressBook(request);

        assertEquals(expected, result);
        assertNotNull(result);
    }

    @Test
    void getAddressBooks_shouldReturnAddressBooks() {
        //given
        AddressBook addressBook1 = new AddressBook("WORK");
        AddressBook addressBook2 = new AddressBook("HOME");
        List<AddressBook> expected = List.of(addressBook1, addressBook2);

        //when
        when(addressBookRepository.findAll()).thenReturn(expected);

        //then
        List<AddressBook> result = addressBookService.getAddressBooks();

        assertNotNull(result);
        assertEquals(expected.size(), result.size());
    }

    @Test
    void deleteAddressBookById_shouldDeleteAddressBook() {
        //given
        AddressBook addressBook = new AddressBook("WORK");

        //when
        when(addressBookRepository.findById(any())).thenReturn(Optional.of(addressBook));

        //then
        addressBookService.deleteAddressBookById(1);

        assertEquals(0, addressBook.getContacts().size());
    }

    @Test
    void deleteAddressBookById_shouldThrowException_whenAddressBookNotFound() {
        //when
        when(addressBookRepository.findById(any())).thenReturn(Optional.empty());

        //then & assert
        assertThrows(DataNotFoundException.class, () -> addressBookService.deleteAddressBookById(42));
    }

    @Test
    void createContact_shouldReturnContactInAddressBook() {
        //given
        AddressBook addressBook = new AddressBook("WORK");
        ContactRequest request = new ContactRequest("test", List.of("0402465555"));
        Contact expected = new Contact("test");

        //when
        when(addressBookRepository.findById(any())).thenReturn(Optional.of(addressBook));
        when(contactService.createContact(any())).thenReturn(expected);

        //then
        Contact result = addressBookService.createContact(1, request);

        assertNotNull(addressBook.getContacts());
        assertTrue(addressBook.getContacts().contains(result));
        assertEquals(expected, result);
        assertNotNull(result);
    }

    @Test
    void getContactsByAddressBookId_shouldReturnContacts() {
        //given
        AddressBook addressBook = new AddressBook("WORK");
        Contact contact1 = new Contact("test 1");
        Contact contact2 = new Contact("test 2");
        addressBook.getContacts().addAll(Set.of(contact1, contact2));

        //when
        when(addressBookRepository.findById(any())).thenReturn(Optional.of(addressBook));

        //then
        var result = addressBookService.getContactsByAddressId(1);

        assertNotNull(result);
        assertTrue(result.contains(contact1));
        assertTrue(result.contains(contact2));
    }
}
