package com.reece.addressbookservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
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
}
