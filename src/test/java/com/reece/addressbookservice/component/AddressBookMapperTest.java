package com.reece.addressbookservice.component;

import com.reece.addressbookservice.dto.AddressBookRequest;
import com.reece.addressbookservice.dto.AddressBookResponse;
import com.reece.addressbookservice.entity.AddressBook;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AddressBookMapperTest {

    private final AddressBookMapper addressBookMapper = new AddressBookMapper();

    @Test
    void toAddressBookEntity_shouldTransformAddressBookRequestToAddressBook() {
        //given
        AddressBookRequest request = new AddressBookRequest("test");

        //when
        AddressBook addressBook = addressBookMapper.toAddressBookEntity(request);

        //then
        assertNotNull(addressBook);
        assertEquals("test", addressBook.getName());
    }

    @Test
    void toAddressBookResponse_shouldTransformAddressBookToAddressBookResponse() {
        //given
        AddressBook addressBook = new AddressBook(1, "test");

        //when
        AddressBookResponse response = addressBookMapper.toAddressBookResponse(addressBook);

        //then
        assertNotNull(response);
        assertEquals(1, response.id());
        assertEquals("test", response.name());
    }
}