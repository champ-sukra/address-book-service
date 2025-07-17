package com.reece.addressbookservice.application.mapper;

import com.reece.addressbookservice.application.mapper.PhoneNoMapper;
import com.reece.addressbookservice.application.mapper.PhoneNoMapperImpl;
import com.reece.addressbookservice.presentation.dto.PhoneNoRequest;
import com.reece.addressbookservice.domain.entity.PhoneNo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhoneNoMapperImplTest {

    private final PhoneNoMapper phoneNoMapper = new PhoneNoMapperImpl();

    @Test
    void toPhoneNoEntity_shouldMapPhoneNoRequestToPhoneNoEntity() {
        //given
        PhoneNoRequest request = new PhoneNoRequest("0401234567");

        //when
        PhoneNo phoneNo = phoneNoMapper.toPhoneNoEntity(request);

        //then
        assertNotNull(phoneNo);
        assertEquals("0401234567", phoneNo.getNumber());
        assertNull(phoneNo.getContact());
        assertNull(phoneNo.getId());
    }
}