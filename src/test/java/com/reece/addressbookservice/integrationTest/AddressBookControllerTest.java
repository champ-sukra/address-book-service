package com.reece.addressbookservice.integrationTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.addressbookservice.presentation.dto.AddressBookRequest;
import com.reece.addressbookservice.presentation.dto.ContactRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddressBookControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fullAddressBookWithContactFlow_shouldWorkCorrectly() throws Exception {
        //create address-book
        AddressBookRequest addressBookRequest = new AddressBookRequest("integration-book");
        ResponseEntity<String> createAbResp = restTemplate.postForEntity(
                "/address-books", addressBookRequest, String.class);

        assertEquals(HttpStatus.CREATED, createAbResp.getStatusCode());

        //parse the response to get the address-book ID
        JsonNode abRoot = objectMapper.readTree(createAbResp.getBody());
        int addressBookId = abRoot.path("data").path("id").asInt();
        assertTrue(addressBookId > 0);

        //create contact using the returning address-book-id
        ContactRequest contactRequest = new ContactRequest("Test Contact", null);
        ResponseEntity<String> createContactResp = restTemplate.postForEntity(
                "/address-books/" + addressBookId + "/contacts", contactRequest, String.class);

        assertEquals(HttpStatus.CREATED, createContactResp.getStatusCode());

        //get contacts from the address-book
        ResponseEntity<String> getContactsResp = restTemplate.getForEntity(
                "/address-books/" + addressBookId + "/contacts", String.class);

        assertEquals(HttpStatus.OK, getContactsResp.getStatusCode());
        JsonNode contactsRoot = objectMapper.readTree(getContactsResp.getBody());
        assertEquals(1, contactsRoot.path("data").size());
        assertEquals("Test Contact", contactsRoot.path("data").get(0).path("name").asText());

        //delete the address-book
        restTemplate.delete("/address-books/" + addressBookId);

        //confirm the address-book is deleted
        ResponseEntity<String> afterDeleteResp = restTemplate.getForEntity(
                "/address-books/" + addressBookId + "/contacts", String.class);

        assertEquals(HttpStatus.NOT_FOUND, afterDeleteResp.getStatusCode());
    }
}
