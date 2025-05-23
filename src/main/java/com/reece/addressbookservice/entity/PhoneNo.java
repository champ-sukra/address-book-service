package com.reece.addressbookservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class PhoneNo {
    @Id
    @GeneratedValue
    private Long id;

    private String number;

    @ManyToOne
    @JoinColumn(name = "contact_id", nullable = false)
    @JsonBackReference
    private Contact contact;

    protected PhoneNo() {}

    public PhoneNo(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhoneNo phoneNo)) return false;
        return Objects.equals(number, phoneNo.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
