package com.itmo.phone_book;

import java.util.List;

public interface Storage {
    Contact save(Contact contact);
    Contact remove(int id);
    Contact find(int id);
    List<Contact> find();
    List<Contact> find(String keyword);
}
