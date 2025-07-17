package com.reece.addressbookservice.domain.service;

import com.reece.addressbookservice.application.mapper.ContactMapper;
import com.reece.addressbookservice.application.mapper.PhoneNoMapper;
import com.reece.addressbookservice.domain.service.ContactServiceImpl;
import com.reece.addressbookservice.presentation.dto.ContactRequest;
import com.reece.addressbookservice.presentation.dto.PhoneNoRequest;
import com.reece.addressbookservice.domain.entity.Contact;
import com.reece.addressbookservice.domain.entity.PhoneNo;
import com.reece.addressbookservice.presentation.exception.DataNotFoundException;
import com.reece.addressbookservice.infrastructure.persistance.ContactRepository;
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
        Contact contact = new Contact("test");
        Contact expected = new Contact("test");

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
        Contact expected = new Contact("test");

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
        Contact expected1 = new Contact("test 1");
        Contact expected2 = new Contact("test 2");

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
        Contact contact = new Contact("test");

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
        Contact expected = new Contact("test");
        PhoneNoRequest request = new PhoneNoRequest("0402465555");
        PhoneNo phoneNo = new PhoneNo("0402465555");

        when(contactRepository.findById(anyLong())).thenReturn(Optional.of(expected));
        when(phoneNoMapper.toPhoneNoEntity(request)).thenReturn(phoneNo);
        when(contactRepository.save(expected)).thenReturn(expected);

        //when
        Contact result = contactService.addPhoneNoToContact(1L, request);

        assertEquals(expected, result);
        verify(contactRepository).save(expected);
    }

}

