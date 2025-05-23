package com.reece.addressbookservice.component;

import com.reece.addressbookservice.dto.AddressBookRequest;
import com.reece.addressbookservice.dto.AddressBookResponse;
import com.reece.addressbookservice.dto.PhoneNoRequest;
import com.reece.addressbookservice.entity.AddressBook;
import com.reece.addressbookservice.entity.PhoneNo;
import org.springframework.stereotype.Component;

@Component
public class PhoneNoMapper {
    public PhoneNo toPhoneNoEntity(PhoneNoRequest request) {
        return new PhoneNo(null, request.phoneNo());
    }
}
