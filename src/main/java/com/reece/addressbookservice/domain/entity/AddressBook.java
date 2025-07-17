package com.reece.addressbookservice.domain.entity;

import com.reece.addressbookservice.presentation.exception.InvalidInputException;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class AddressBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @ManyToMany
    @JoinTable(
            name = "address_book_contact",
            joinColumns = @JoinColumn(name = "address_book_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_id")
    )
    private Set<Contact> contacts = new HashSet<>();


    protected AddressBook() {}

    public AddressBook(String name) {
        if (name.length() > 20) {
            throw new InvalidInputException("invalid_address_book_name", "Address book name cannot exceed 20 characters");
        }
        if (name.contains("@") || name.contains("#")) {
            throw new InvalidInputException("invalid_address_book_name", "Address book name cannot contain special characters");
        }
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Contact> getContacts() {
        return contacts;
    }
}
