package com.itmo.phone_book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryStorage implements Storage {
    private final List<Contact> contacts = new ArrayList<>();
    private int ids;

    @Override
    public Contact save(Contact newContact) {
        if (newContact.getId() == 0) {
            // add
            newContact.setId(++ids);
            contacts.add(newContact);

            return newContact;
        } else {
            // update
            for (Contact actualContact : contacts) {
                if (actualContact.getId() == newContact.getId()) {
                    actualContact.setValues(newContact);

                    return newContact;
                }
            }

            newContact.setId(0);

            return save(newContact);
        }
    }

    @Override
    public Contact remove(int id) {
        return null;
    }

    @Override
    public Optional<Contact> find(int id) {
        return contacts.stream()
                .filter(c -> c.getId() == id)
                .findAny();
    }

    @Override
    public List<Contact> find() {
        return new ArrayList<>(contacts);
    }

    @Override
    public List<Contact> find(String keyword) {
        List<Contact> result = new ArrayList<>();
        for (Contact contact : contacts) {
            if (contains(contact, keyword)) {
                result.add(contact);
            }
        }

        return result;
    }

    private boolean contains(Contact contact, String keyword) {
        return contains(contact.getName(), keyword)
                || contains(contact.getAddress(), keyword)
                || contains(contact.getPhones(), keyword);
    }

    private boolean contains(/* nullable */ String string, String keyword) {
        return string != null
                && string.toLowerCase().contains(keyword.toLowerCase());
    }
}
