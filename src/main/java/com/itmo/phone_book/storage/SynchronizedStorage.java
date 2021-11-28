package com.itmo.phone_book.storage;

import com.itmo.phone_book.model.Contact;

import java.util.List;
import java.util.Optional;

// Используем здесь шаблон Decorator, чтобы можно было применить синхронизирующий функционал к любой
// реализации Storage.
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

    @Override
    public void close() throws Exception {
        storage.close();
    }
}
