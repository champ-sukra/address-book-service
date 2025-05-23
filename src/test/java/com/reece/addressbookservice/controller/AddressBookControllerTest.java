package com.reece.addressbookservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.addressbookservice.dto.AddressBookRequest;
import com.reece.addressbookservice.dto.AddressBookResponse;
import com.reece.addressbookservice.dto.ContactRequest;
import com.reece.addressbookservice.dto.ContactResponse;
import com.reece.addressbookservice.entity.AddressBook;
import com.reece.addressbookservice.entity.Contact;
import com.reece.addressbookservice.exception.DataNotFoundException;
import com.reece.addressbookservice.mapper.AddressBookMapper;
import com.reece.addressbookservice.mapper.ContactMapper;
import com.reece.addressbookservice.service.AddressBookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddressBookController.class)
public class AddressBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AddressBookService addressBookService;

    @MockitoBean
    private AddressBookMapper addressBookMapper;

    @MockitoBean
    private ContactMapper contactMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createAddressBook_shouldReturnCreatedAndAddressBookResponse() throws Exception {
        //given
        AddressBookRequest addressBookRequest = new AddressBookRequest("test");
        AddressBook addressBook = new AddressBook(null, "test");
        AddressBookResponse addressBookResponse = new AddressBookResponse(1, "test");

        //when
        when(addressBookService.createAddressBook(any(AddressBookRequest.class)))
                .thenReturn(addressBook);
        when(addressBookMapper.toAddressBookResponse(addressBook))
                .thenReturn(addressBookResponse);

        //then & assertion
        mockMvc.perform(post("/address-books")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(addressBookRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("test"));
    }

    @Test
    void createAddressBook_shouldReturnBadRequest_whenNameIsEmpty() throws Exception {
        //given
        AddressBookRequest addressBookRequest = new AddressBookRequest("");

        //then & assertion
        mockMvc.perform(post("/address-books")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(addressBookRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("invalid_request"));
    }

    @Test
    void getAddressBooks_shouldReturnOkAndListOfAddressBookResponse() throws Exception {
        //given
        AddressBook addressBook1 = new AddressBook(1, "test 1");
        AddressBook addressBook2 = new AddressBook(2, "test 2");
        AddressBookResponse addressBookResponse1 = new AddressBookResponse(1, "test 1");
        AddressBookResponse addressBookResponse2 = new AddressBookResponse(2, "test 2");

        //when
        when(addressBookService.getAddressBooks())
                .thenReturn(List.of(addressBook1, addressBook2));
        when(addressBookMapper.toAddressBookResponse(addressBook1))
                .thenReturn(addressBookResponse1);
        when(addressBookMapper.toAddressBookResponse(addressBook2))
                .thenReturn(addressBookResponse2);

        //then & assertion
        mockMvc.perform(get("/address-books")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("test 1"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].name").value("test 2"));
    }

    @Test
    void deleteAddressBook_shouldReturnOk() throws Exception {
        //given
        int addressBookId = 1;

        //when
        doNothing().when(addressBookService).deleteAddressBookById(addressBookId);

        //then & assertion
        mockMvc.perform(delete("/address-books/{id}", addressBookId)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"));
    }

    @Test
    void deleteAddressBook_shouldReturnBadRequest_whenAddressBookIdIsZero() throws Exception {
        //given
        int addressBookId = 0;

        //then & assertion
        mockMvc.perform(delete("/address-books/{id}", addressBookId)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteAddressBook_shouldReturnNotFound_whenAddressBookIdDoesNotExist() throws Exception {
        //given
        int addressBookId = 1;

        //when
        doThrow(new DataNotFoundException("ab_not_found", "Address book not found"))
                .when(addressBookService).deleteAddressBookById(addressBookId);

        //then & assertion
        mockMvc.perform(delete("/address-books/{id}", addressBookId)
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("ab_not_found"));
    }

    @Test
    void createContact_shouldReturnCreatedAndContactResponse() throws Exception {
        //given
        int addressBookId = 1;
        ContactRequest request = new ContactRequest("test", null);
        Contact contact = new Contact(1L, "test");
        ContactResponse response = new ContactResponse(1L, "test", null);

        //when
        when(addressBookService.createContact(anyInt(), any(ContactRequest.class)))
                .thenReturn(contact);
        when(contactMapper.toContactResponse(any(Contact.class)))
                .thenReturn(response);

        //then & assertion
        mockMvc.perform(post("/address-books/{id}/contacts", addressBookId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("success"))
                .andExpect(jsonPath("$.data.id").value(addressBookId))
                .andExpect(jsonPath("$.data.name").value("test"));
    }

    @Test
    void createContact_shouldReturnBadRequest_whenAddressBookIdIsZero() throws Exception {
        //given
        int addressBookId = 0;
        ContactRequest request = new ContactRequest("test", null);

        //then & assertion
        mockMvc.perform(post("/address-books/{id}/contacts", addressBookId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createContact_shouldReturnBadRequest_whenContactRequestIsInvalid() throws Exception {
        //given
        int addressBookId = 1;
        ContactRequest request = new ContactRequest("", null);

        //then & assertion
        mockMvc.perform(post("/address-books/{id}/contacts", addressBookId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("invalid_request"));
    }

    @Test
    void createContact_shouldReturnNotFound_whenAddressBookDoesNotExist() throws Exception {
        //given
        int addressBookId = 1;
        ContactRequest request = new ContactRequest("test", null);

        //when
        doThrow(new DataNotFoundException("ab_not_found", "Address book not found"))
                .when(addressBookService).createContact(addressBookId, request);

        //then & assertion
        mockMvc.perform(post("/address-books/{id}/contacts", addressBookId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("ab_not_found"));
    }

    @Test
    void getContactsByAddressId_shouldReturnOkAndContactList() throws Exception {
        //given
        int addressBookId = 1;
        Contact contact1 = new Contact(1L, "test 1");
        Contact contact2 = new Contact(2L, "test 2");

        //when
        when(addressBookService.getContactsByAddressId(addressBookId))
                .thenReturn(Set.of(contact1, contact2));
        when(contactMapper.toContactResponse(contact1))
                .thenReturn(new ContactResponse(1L, "test 1", null));
        when(contactMapper.toContactResponse(contact2))
                .thenReturn(new ContactResponse(2L, "test 2", null));

        //then & assertion
        mockMvc.perform(get("/address-books/{id}/contacts", addressBookId)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andExpect(jsonPath("$.data.size()").value(2));
    }

    @Test
    void getContactsByAddressId_shouldReturnNotFound_whenAddressBookDoesNotExist() throws Exception {
        //given
        int addressBookId = 1;

        //when
        when(addressBookService.getContactsByAddressId(addressBookId))
                .thenThrow(new DataNotFoundException("ab_not_found", "Address book not found"));

        //then & assertion
        mockMvc.perform(get("/address-books/{id}/contacts", addressBookId)
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("ab_not_found"));
    }

    @Test
    void getContactsByAddressId_shouldReturnBadRequest_whenAddressBookIdIsZero() throws Exception {
        //given
        int addressBookId = 0;

        //then & assertion
        mockMvc.perform(get("/address-books/{id}/contacts", addressBookId)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }
}
