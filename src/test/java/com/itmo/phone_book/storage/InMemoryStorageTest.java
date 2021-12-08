package com.itmo.phone_book.storage;

import com.itmo.phone_book.model.Contact;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryStorageTest {

    private InMemoryStorage storage;

    @BeforeEach
    void setUp() {
        storage = new InMemoryStorage();
    }

    @Test
    public void shouldReturnEmptyListOnInitialFind() {
        final List<Contact> contacts = storage.find();

//        assertNotNull(contacts);
//        assertTrue(contacts.isEmpty());

//        MatcherAssert.assertThat(contacts, Matchers.empty());
        assertThat(contacts, empty());
    }

    @Test
    public void shouldSaveAndFindAll() {

    }
}