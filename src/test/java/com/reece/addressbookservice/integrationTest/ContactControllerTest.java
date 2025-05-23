package com.reece.addressbookservice.integrationTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.addressbookservice.dto.ContactRequest;
import com.reece.addressbookservice.dto.PhoneNoRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ContactControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fullContactFlow_shouldWorkCorrectly() throws Exception {
        //create a contact
        ContactRequest contactRequest = new ContactRequest("test", List.of("0402465555"));
        ResponseEntity<String> createContactResp = restTemplate.postForEntity(
                "/address-books/1/contacts", contactRequest, String.class);

        assertEquals(HttpStatus.CREATED, createContactResp.getStatusCode());

        //parse the response to get the contact ID
        JsonNode contactRoot = objectMapper.readTree(createContactResp.getBody());
        int contactId = contactRoot.path("data").path("id").asInt();
        assertEquals(1, contactId);

        //get the contact from the contact-id
        ResponseEntity<String> getContactResp = restTemplate.getForEntity(
                "/contacts/" + contactId, String.class);

        assertEquals(HttpStatus.OK, getContactResp.getStatusCode());
        JsonNode getContactRoot = objectMapper.readTree(getContactResp.getBody());
        assertEquals("test", getContactRoot.path("data").path("name").asText());
        assertEquals("0402465555",
                getContactRoot.path("data").path("phone_nos").get(0).path("number").asText());

        //add a duplicate contact
        ContactRequest duplicateRequest = new ContactRequest("test", List.of("0402465555"));
        ResponseEntity<String> duplicateContactResp = restTemplate.postForEntity(
                "/address-books/1/contacts", duplicateRequest, String.class);

        assertEquals(HttpStatus.CREATED, duplicateContactResp.getStatusCode());

        //ensure get unique contacts
        ResponseEntity<String> uniqueContactsResp = restTemplate.getForEntity("/contacts/unique", String.class);
        assertEquals(HttpStatus.OK, uniqueContactsResp.getStatusCode());
        JsonNode uniqueRoot = objectMapper.readTree(uniqueContactsResp.getBody());
        assertEquals(1, uniqueRoot.path("data").size());

        //add new phone number to the existing contact
        PhoneNoRequest phoneNoRequest = new PhoneNoRequest("0402465556");
        ResponseEntity<String> addPhoneResp = restTemplate.exchange(
                "/contacts/" + contactId + "/phone-nos",
                HttpMethod.PATCH,
                new HttpEntity<>(phoneNoRequest),
                String.class
        );

        assertEquals(HttpStatus.OK, addPhoneResp.getStatusCode());

        //delete the contact
        restTemplate.delete("/contacts/" + contactId);

        //check if the contact is deleted
        ResponseEntity<String> afterDeleteResp = restTemplate.getForEntity(
                "/contacts/" + contactId, String.class);

        assertEquals(HttpStatus.NOT_FOUND, afterDeleteResp.getStatusCode());
    }
}
