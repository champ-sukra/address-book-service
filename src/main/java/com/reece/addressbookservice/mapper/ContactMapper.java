package com.reece.addressbookservice.mapper;

import com.reece.addressbookservice.dto.ContactRequest;
import com.reece.addressbookservice.dto.ContactResponse;
import com.reece.addressbookservice.entity.Contact;
import com.reece.addressbookservice.entity.PhoneNo;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper {
    public ContactResponse toContactResponse(Contact contact) {
        return new ContactResponse(
                contact.getId(),
                contact.getName(),
                contact.getPhoneNos()
        );
    }

    public Contact toContactEntity(ContactRequest contactRequest) {
        Contact contact = new Contact(null, contactRequest.name());
        if (contactRequest.phoneNos() != null) {
            // Map each phone number string to a PhoneNo entity and associate it
            for (String num : contactRequest.phoneNos()) {
                PhoneNo phoneNo = new PhoneNo(null, num);
                contact.addPhoneNo(phoneNo);
            }
        }
        return contact;
    }
}
