package com.itmo.phone_book;

public class Contact {
    private int id;
    private String name;
    private String address;
    private String phones;

    public Contact() {
    }

    public Contact(int id, String name, String address, String phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    public void setValues(Contact other) {
        setName(other.getName());
        setAddress(other.getAddress());
        setPhones(other.getPhones());
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phones='" + phones + '\'' +
                '}';
    }
}
