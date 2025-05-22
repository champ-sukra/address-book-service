package com.reece.addressbookservice.component;

import com.reece.addressbookservice.dto.ContactRequest;
import com.reece.addressbookservice.dto.ContactResponse;
import com.reece.addressbookservice.entity.Contact;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper {
    public ContactResponse toContactResponse(Contact contact) {
        return new ContactResponse(
                contact.getId(),
                contact.getName(),
                contact.getPhoneNumbers()
        );
    }

    public Contact toContactEntity(ContactRequest contactRequest) {
        return new Contact(
                null,
                contactRequest.name()
        );
    }
}
