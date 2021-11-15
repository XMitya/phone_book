package com.itmo.phone_book;

import java.util.Scanner;

public class PhoneBook {
    private static final String HELP_STRING = """
            Команды:
            find [keyword]
            add
            update <id>
            quit
            help
            """;
    private final Storage storage;

    public PhoneBook(Storage storage) {
        this.storage = storage;
    }

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook(createStorage(args));
        phoneBook.start();
    }

    public void start() {
        help();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();
            if (isBlank(command)) {
                System.err.println("Команды не указана");
                help();

                continue;
            }

            String[] commandArgs = command.split("\s");
            switch (commandArgs[0]) {
                case "help":
                    help();
                    break;
                case "add":
                    add(scanner);
                    break;
                case "quit":
                    System.out.println("Bye");
                    return;
                default:
                    System.err.println("Неизвестная команда: " + commandArgs[0]);
                    help();
                    break;
            }
        }
    }

    private void add(Scanner scanner) {
        Contact newContact = createDefaultContact();
        fillContact(scanner, newContact);

        Contact savedContact = storage.save(newContact);
        System.out.println("Добавлен контакт:");
        System.out.println(savedContact);
    }

    private void fillContact(Scanner scanner, Contact newContact) {
        System.out.println("Name [" + newContact.getName() +"]:");
        String name = scanner.nextLine();
        if (!isBlank(name)) {
            newContact.setName(name);
        }

        System.out.println("Address [" + newContact.getAddress() +"]:");
        String address = scanner.nextLine();
        if (!isBlank(address)) {
            newContact.setAddress(address);
        }

        System.out.println("Phones [" + newContact.getPhones() +"]:");
        String phones = scanner.nextLine();
        if (!isBlank(phones)) {
            newContact.setPhones(phones);
        }
    }

    private void help() {
        System.out.println(HELP_STRING);
    }
    
    private boolean isBlank(String string) {
        return string == null || string.isBlank();
    }
    
    private Contact createDefaultContact() {
        return new Contact(0, "Anonymous", "Missed address", "666");
    }
    
    private static Storage createStorage(String[] args) {
        if (args.length == 0) {
            return new InMemoryStorage();
        }
        
        // Здесь будет создание других реализаций Storage
        return null;
    }
}
