package com.reece.addressbookservice.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.reece.addressbookservice.presentation.exception.InvalidInputException;
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

    public PhoneNo(String number) {
        validateNumber(number);

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

    private void validateNumber(String number) {
        if (!isValidPhoneNumber(number)) {
            throw new InvalidInputException("invalid_phone_number",
                    "Phone number must be between 8 and 15 digits long and valid in format");
        }
    }

    private boolean isValidPhoneNumber(String number) {
        return number != null && number.matches("\\+?[0-9\\s\\-\\(\\)]{8,15}");
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
