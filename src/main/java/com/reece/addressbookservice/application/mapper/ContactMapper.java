package com.reece.addressbookservice.application.mapper;

import com.reece.addressbookservice.domain.entity.Contact;
import com.reece.addressbookservice.presentation.dto.ContactRequest;
import com.reece.addressbookservice.presentation.dto.ContactResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface  ContactMapper {
    default ContactResponse toContactResponse(Contact contact) {
        return new ContactResponse(
                contact.getId(),
                contact.getName(),
                contact.getPhoneNos()
        );
    }

    default Contact toContactEntity(ContactRequest contactRequest) {
        Contact contact = new Contact(contactRequest.name());
        contact.updateContactDetails(contactRequest.phoneNos());
        return contact;
    }
}
