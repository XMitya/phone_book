package com.itmo.phone_book;

import java.util.List;
import java.util.Optional;

public class SynchronizedStorage implements Storage {
    private final Storage storage;

    public SynchronizedStorage(Storage storage) {
        this.storage = storage;
    }

    @Override
    public synchronized Contact save(Contact contact) {
        return storage.save(contact);
    }

    @Override
    public synchronized Contact remove(int id) {
        return storage.remove(id);
    }

    @Override
    public synchronized Optional<Contact> find(int id) {
        return storage.find(id);
    }

    @Override
    public synchronized List<Contact> find() {
        return storage.find();
    }

    @Override
    public synchronized List<Contact> find(String keyword) {
        return storage.find(keyword);
    }
}
