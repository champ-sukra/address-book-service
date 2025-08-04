package com.reece.addressbookservice.application.mapper;

import com.reece.addressbookservice.application.ContactMapperImpl;
import com.reece.addressbookservice.presentation.dto.ContactRequest;
import com.reece.addressbookservice.presentation.dto.ContactResponse;
import com.reece.addressbookservice.domain.entity.Contact;
import com.reece.addressbookservice.domain.entity.PhoneNo;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ContactMapperImplTest {

    private final ContactMapper contactMapper = new ContactMapperImpl();

    @Test
    void toContactEntity_shouldTransformContactRequestToContact() {
        //given
        ContactRequest request = new ContactRequest("test", List.of("0402465555", "0402465556"));

        //when
        Contact contact = contactMapper.toContactEntity(request);

        //then
        assertNotNull(contact);
        assertEquals("test", contact.getName());
        assertNotNull(contact.getPhoneNos());
        assertEquals(2, contact.getPhoneNos().size());
        assertEquals(Set.of("0402465555", "0402465556"),
                contact.getPhoneNos().stream().map(PhoneNo::getNumber).collect(Collectors.toSet()));
    }

    @Test
    void toContactResponse_shouldTransformContactToContactResponse() {
        //given
        Contact contact = new Contact("test");
        contact.addPhoneNo(new PhoneNo("0402465555"));
        contact.addPhoneNo(new PhoneNo("0402465556"));
        contact.addPhoneNo(new PhoneNo("0402465557"));

        //when
        ContactResponse response = contactMapper.toContactResponse(contact);

        //then
        assertNotNull(response);
        assertEquals("test", response.name());
        assertNotNull(response.phoneNos());
        assertEquals(3, response.phoneNos().size());
        assertEquals(List.of("0402465555", "0402465556", "0402465557"),
                response.phoneNos().stream().map(PhoneNo::getNumber).sorted().collect(Collectors.toList()));
    }
}