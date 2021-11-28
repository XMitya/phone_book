package com.itmo.phone_book.server.message;

import com.itmo.phone_book.model.Contact;

import java.util.List;

public class Success implements Response {
    private final List<Contact> contacts;

    public Success(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public List<Contact> getContacts() {
        return contacts;
    }
}
