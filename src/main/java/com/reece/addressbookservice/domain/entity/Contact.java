package com.reece.addressbookservice.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.reece.addressbookservice.presentation.exception.InvalidInputException;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Contact {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "contacts")
    @JsonBackReference
    private Set<AddressBook> addressBooks = new HashSet<>();

    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PhoneNo> phoneNos = new HashSet<>();

    protected Contact() {}

    public Contact(String name) {
        if (name.length() > 100) {
            throw new InvalidInputException("invalid_contact_name", "Contact name cannot exceed 100 characters");
        }
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<PhoneNo> getPhoneNos() {
        return phoneNos;
    }

    public Set<AddressBook> getAddressBooks() {
        return addressBooks;
    }

    public void addPhoneNo(PhoneNo phoneNo) {
        if (this.phoneNos == null) {
            this.phoneNos = new HashSet<>();
        }
        phoneNos.add(phoneNo);
        phoneNo.setContact(this);
    }

    public void removePhoneNo(PhoneNo phoneNo) {
        phoneNos.remove(phoneNo);
        phoneNo.setContact(this);
    }

    public void updateContactDetails(List<String> phoneNos) {
        if (phoneNos != null) {
            // Map each phone number string to a PhoneNo entity and associate it
            for (String num : phoneNos) {
                PhoneNo phoneNo = new PhoneNo(num);
                this.addPhoneNo(phoneNo);
            }
        }
    }
}
