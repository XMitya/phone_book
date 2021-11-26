package com.itmo.phone_book.server;

import com.itmo.phone_book.Contact;
import com.itmo.phone_book.Storage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server {
    private final Storage storage;
    private final int port;

    public Server(Storage storage, int port) {
        this.storage = storage;
        this.port = port;
    }

    public void start() {
        new ConnectionListener().start();
    }

    private class ConnectionListener extends Thread {
        @Override
        public void run() {
            try (ServerSocket ssocket = new ServerSocket(port)) {
                while (!isInterrupted()) {
                    final Socket socket = ssocket.accept();

                    new ConnectionServer(socket).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ConnectionServer extends Thread {
        private final Socket socket;

        private ConnectionServer(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (socket;
                final var objIn = new ObjectInputStream(socket.getInputStream());
                final var objOut = new ObjectOutputStream(socket.getOutputStream())) {
                while (!isInterrupted()) {
                    final Command command = (Command) objIn.readObject();
                    if (command instanceof Save save) {
                        final Contact saved = storage.save(save.getContact());

                        objOut.writeObject(new Success(List.of(saved)));
                    } else if (command instanceof Find find) {
                        final List<Contact> found = storage.find(find.getKeyword());

                        objOut.writeObject(new Success(found));
                    } else {
                        objOut.writeObject(new Failure("Unknown command"));
                    }
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
