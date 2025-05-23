package com.reece.addressbookservice.service;

import com.reece.addressbookservice.component.ContactMapper;
import com.reece.addressbookservice.component.PhoneNoMapper;
import com.reece.addressbookservice.dto.ContactRequest;
import com.reece.addressbookservice.dto.PhoneNoRequest;
import com.reece.addressbookservice.entity.Contact;
import com.reece.addressbookservice.entity.PhoneNo;
import com.reece.addressbookservice.exception.DataNotFoundException;
import com.reece.addressbookservice.repository.ContactRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContactServiceTest {

    @Mock
    ContactMapper contactMapper;

    @Mock
    PhoneNoMapper phoneNoMapper;

    @Mock
    ContactRepository contactRepository;

    @InjectMocks
    ContactServiceImpl contactService;

    @Test
    void createContact_shouldReturnContact() {
        //given
        ContactRequest request = new ContactRequest("test", null);
        Contact contact = new Contact(null, "test");
        Contact expected = new Contact(1L, "test");

        //when
        when(contactMapper.toContactEntity(request)).thenReturn(contact);
        when(contactRepository.save(contact)).thenReturn(expected);

        //then
        Contact result = contactService.createContact(request);

        assertEquals(expected, result);
        verify(contactRepository).save(contact);
    }

    @Test
    void getContactDetail_shouldReturnContact() {
        //given
        Contact expected = new Contact(1L, "test");

        //when
        when(contactRepository.findById(1L)).thenReturn(Optional.of(expected));

        //then
        Contact result = contactService.getContactDetail(1L);

        assertEquals(expected, result);
        verify(contactRepository).findById(1L);
    }

    @Test
    void getContactDetail_shouldThrowException_whenContactNotFound() {
        //when
        when(contactRepository.findById(anyLong())).thenReturn(Optional.empty());

        //then & assert
        assertThrows(DataNotFoundException.class, () -> contactService.getContactDetail(42L));
    }

    @Test
    void getAllContacts_shouldReturnContactList() {
        //given
        Contact expected1 = new Contact(1L, "test 1");
        Contact expected2 = new Contact(2L, "test 2");

        //when
        when(contactRepository.findAll()).thenReturn(List.of(expected1, expected2));

        //then
        List<Contact> result = contactService.getAllContacts();

        assertEquals(2, result.size());
        assertEquals(expected1, result.get(0));
        assertEquals(expected2, result.get(1));
        verify(contactRepository).findAll();
    }

    @Test
    void deleteContact_shouldDeleteContact() {
        //given
        Contact contact = new Contact(1L, "test");

        //when
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));

        //then
        contactService.deleteContact(1L);

        verify(contactRepository).delete(contact);
    }

    @Test
    void deleteContact_shouldThrowException_whenContactNotFound() {
        //when
        when(contactRepository.findById(anyLong())).thenReturn(Optional.empty());

        //then & assert
        assertThrows(DataNotFoundException.class, () -> contactService.deleteContact(42L));
    }

    @Test
    void addPhoneNoToContact_shouldReturnContact() {
        //given
        Contact expected = new Contact(1L, "test");
        PhoneNoRequest request = new PhoneNoRequest("0402465555");
        PhoneNo phoneNo = new PhoneNo(null, "0402465555");

        when(contactRepository.findById(anyLong())).thenReturn(Optional.of(expected));
        when(phoneNoMapper.toPhoneNoEntity(request)).thenReturn(phoneNo);
        when(contactRepository.save(expected)).thenReturn(expected);

        //when
        Contact result = contactService.addPhoneNoToContact(1L, request);

        assertEquals(expected, result);
        verify(contactRepository).save(expected);
    }

}

