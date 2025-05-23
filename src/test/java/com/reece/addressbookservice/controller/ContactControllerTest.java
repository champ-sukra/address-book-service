package com.reece.addressbookservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.addressbookservice.dto.ContactRequest;
import com.reece.addressbookservice.dto.ContactResponse;
import com.reece.addressbookservice.dto.PhoneNoRequest;
import com.reece.addressbookservice.entity.Contact;
import com.reece.addressbookservice.entity.PhoneNo;
import com.reece.addressbookservice.exception.DataNotFoundException;
import com.reece.addressbookservice.mapper.ContactMapper;
import com.reece.addressbookservice.service.ContactService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContactController.class)
public class ContactControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContactService contactService;

    @MockitoBean
    private ContactMapper contactMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getContactById_shouldReturnContact() throws Exception {
        //given
        Long contactId = 1L;
        Contact contact = new Contact(1L, "test");
        PhoneNo phoneNo = new PhoneNo(null, "0402465555");
        ContactResponse response = new ContactResponse(1L, "test", Set.of(phoneNo));

        //when
        when(contactService.getContactDetail(anyLong()))
                .thenReturn(contact);
        when(contactMapper.toContactResponse(any(Contact.class)))
                .thenReturn(response);

        //then & assertion
        mockMvc.perform(get("/contacts/{id}", contactId)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("test"))
                .andExpect(jsonPath("$.data.phone_nos[0].number").value("0402465555"));
    }

    @Test
    void getContactById_shouldReturnBadRequest_whenContactIdIsZero() throws Exception {
        //given
        Long contactId = 0L;

        //when & then
        mockMvc.perform(get("/contacts/{id}", contactId)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getContactById_shouldReturnBadRequest_whenContactIdIsNegative() throws Exception {
        //given
        Long contactId = -1L;

        //when & then
        mockMvc.perform(get("/contacts/{id}", contactId)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getContactById_shouldReturnNotFound_whenContactNotFound() throws Exception {
        //given
        Long contactId = 1L;

        //when
        when(contactService.getContactDetail(anyLong()))
                .thenThrow(new DataNotFoundException(
                        "contact_not_found",
                        String.format("Contact - %d is not found", contactId)));

        //then & assertion
        mockMvc.perform(get("/contacts/{id}", contactId)
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUniqueContacts_shouldReturnUniqueContacts() throws Exception {
        //given
        Contact contact1 = new Contact(1L, "test");
        Contact contact2 = new Contact(2L, "test");
        PhoneNo phoneNo = new PhoneNo(null, "0402465555");
        contact1.addPhoneNo(phoneNo);
        contact2.addPhoneNo(phoneNo);
        ContactResponse response1 = new ContactResponse(1L, "test", Set.of(phoneNo));
        ContactResponse response2 = new ContactResponse(2L, "test", Set.of(phoneNo));

        //when
        when(contactService.getAllContacts()).thenReturn(List.of(contact1, contact2));
        when(contactMapper.toContactResponse(contact1)).thenReturn(response1);
        when(contactMapper.toContactResponse(contact2)).thenReturn(response2);

        //then & assertion
        mockMvc.perform(get("/contacts/unique")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andExpect(jsonPath("$.data.size()").value(1))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].name").value("test"))
                .andExpect(jsonPath("$.data[0].phone_nos[0].number").value("0402465555"));
    }

    @Test
    void getUniqueContacts_shouldReturnEmptySet_whenNoContacts() throws Exception {
        //when
        when(contactService.getAllContacts()).thenReturn(List.of());

        //then & assertion
        mockMvc.perform(get("/contacts/unique")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andExpect(jsonPath("$.data.size()").value(0));
    }

    @Test
    void deleteContactById_shouldReturnOk() throws Exception {
        //given
        Long contactId = 1L;

        //when
        when(contactService.getContactDetail(anyLong()))
                .thenReturn(new Contact(contactId, "test"));

        //then & assertion
        mockMvc.perform(delete("/contacts/{id}", contactId)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"));
    }

    @Test
    void deleteContactById_shouldReturnBadRequest_whenContactIdIsZero() throws Exception {
        //given
        Long contactId = 0L;

        //when & then
        mockMvc.perform(delete("/contacts/{id}", contactId)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteContactById_shouldReturnBadRequest_whenContactIdIsNegative() throws Exception {
        //given
        Long contactId = -1L;

        //when & then
        mockMvc.perform(delete("/contacts/{id}", contactId)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteContactById_shouldReturnNotFound_whenContactNotFound() throws Exception {
        //given
        Long contactId = 1L;

        //when
        doThrow(new DataNotFoundException("contact_not_found", String.format("Contact - %d is not found", contactId)))
                .when(contactService).deleteContact(anyLong());

        //then & assertion
        mockMvc.perform(delete("/contacts/{id}", contactId)
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addPhoneNoToContact_shouldReturnCreated() throws Exception {
        //given
        Long contactId = 1L;
        Contact contact = new Contact(1L, "test");
        PhoneNo phoneNo = new PhoneNo(null, "0402465555");
        ContactResponse response = new ContactResponse(1L, "test", Set.of(phoneNo));

        //when
        when(contactService.addPhoneNoToContact(anyLong(), any(PhoneNoRequest.class)))
                .thenReturn(contact);
        when(contactMapper.toContactResponse(any(Contact.class)))
                .thenReturn(response);

        //then & assertion
        mockMvc.perform(patch("/contacts/{id}/phone-nos", contactId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new PhoneNoRequest("0402465555"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("success"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("test"))
                .andExpect(jsonPath("$.data.phone_nos[0].number").value("0402465555"));
    }

    @Test
    void addPhoneNoToContact_shouldReturnBadRequest_whenContactIdIsZero() throws Exception {
        //given
        Long contactId = 0L;

        //when & then
        mockMvc.perform(patch("/contacts/{id}/phone-nos", contactId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new PhoneNoRequest("0402465555"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addPhoneNoToContact_shouldReturnBadRequest_whenContactIdIsNegative() throws Exception {
        //given
        Long contactId = -1L;

        //when & then
        mockMvc.perform(patch("/contacts/{id}/phone-nos", contactId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new PhoneNoRequest("0402465555"))))
                .andExpect(status().isBadRequest());
    }
}
