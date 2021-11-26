package com.itmo.phone_book.server;

import com.itmo.phone_book.Contact;

public class Save implements Command {
    private final Contact contact;

    public Save(Contact contact) {
        this.contact = contact;
    }

    public Contact getContact() {
        return contact;
    }
}
