package com.reece.addressbookservice.service;

import com.reece.addressbookservice.component.ContactMapper;
import com.reece.addressbookservice.dto.ContactRequest;
import com.reece.addressbookservice.entity.Contact;
import com.reece.addressbookservice.exception.DataNotFoundException;
import com.reece.addressbookservice.repository.ContactRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContactServiceTest {

    @Mock
    ContactMapper contactMapper;

    @Mock
    ContactRepository contactRepository;

    @InjectMocks
    ContactServiceImpl contactService;

    @Test
    void createContact_shouldReturnContact() {
        //given
        ContactRequest request = new ContactRequest("test", "0402465555");
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
        Contact contact = new Contact(1L, "test");

        //when
        when(contactRepository.findById(1L)).thenReturn(java.util.Optional.of(contact));

        //then
        Contact result = contactService.getContactDetail(1L);

        assertEquals(contact, result);
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
        Contact contact1 = new Contact(1L, "test 1");
        Contact contact2 = new Contact(2L, "test 2");

        //when
        when(contactRepository.findAll()).thenReturn(java.util.List.of(contact1, contact2));

        //then
        java.util.List<Contact> result = contactService.getAllContacts();

        assertEquals(2, result.size());
        assertEquals(contact1, result.get(0));
        assertEquals(contact2, result.get(1));
        verify(contactRepository).findAll();
    }
}

