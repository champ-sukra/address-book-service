package com.reece.addressbookservice.mapper;

import com.reece.addressbookservice.application.mapper.AddressBookMapper;
import com.reece.addressbookservice.application.mapper.AddressBookMapperImpl;
import com.reece.addressbookservice.domain.entity.AddressBook;
import com.reece.addressbookservice.presentation.dto.AddressBookRequest;
import com.reece.addressbookservice.presentation.dto.AddressBookResponse;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AddressBookMapperImplTest {

    private final AddressBookMapper addressBookMapper = new AddressBookMapperImpl();

    @Test
    void toAddressBookEntity_shouldTransformAddressBookRequestToAddressBook() {
        //given
        AddressBookRequest request = new AddressBookRequest("Personal");

        //when
        AddressBook addressBook = addressBookMapper.toAddressBookEntity(request);

        //then
        assertNotNull(addressBook);
        assertEquals("Personal", addressBook.getName());
        assertNotNull(addressBook.getContacts());
        assertEquals(0, addressBook.getContacts().size()); // Expect no contacts initially
    }

    @Test
    void toAddressBookResponse_shouldTransformAddressBookToAddressBookResponse() {
        //given
        AddressBook addressBook = new AddressBook("Personal");
        ReflectionTestUtils.setField(addressBook, "id", 1);

        //when
        AddressBookResponse response = addressBookMapper.toAddressBookResponse(addressBook);

        //then
        assertNotNull(response);
        assertEquals(1, response.id());
        assertEquals("Personal", response.name());
    }
}