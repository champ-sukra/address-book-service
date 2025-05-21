package com.reece.addressbookservice.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @OneToMany
    private Set<PhoneNumber> phoneNumbers = new HashSet<>();

    protected Contact() {

    }

    public Contact(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }
}
