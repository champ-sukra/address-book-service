package com.reece.addressbookservice.component;

import com.reece.addressbookservice.dto.AddressBookRequest;
import com.reece.addressbookservice.dto.AddressBookResponse;
import com.reece.addressbookservice.dto.PhoneNoRequest;
import com.reece.addressbookservice.entity.AddressBook;
import com.reece.addressbookservice.entity.PhoneNo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PhoneNoMapperTest {

    private final PhoneNoMapper phoneNoMapper = new PhoneNoMapper();

    @Test
    void toAddressBookEntity_shouldTransformAddressBookRequestToAddressBook() {
        //given
        PhoneNoRequest request = new PhoneNoRequest("test");

        //when
        PhoneNo phoneNo = phoneNoMapper.toPhoneNoEntity(request);

        //then
        assertNotNull(phoneNo);
        assertEquals("test", phoneNo.getNumber());
    }
}