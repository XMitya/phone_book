package com.itmo.phone_book.server;

import com.itmo.phone_book.model.Contact;
import com.itmo.phone_book.storage.Storage;
import com.itmo.phone_book.server.message.*;
import com.itmo.phone_book.utils.IOUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements AutoCloseable {
    private final Storage storage;
    private final int port;
    private final List<ConnectionServer> connections = new ArrayList<>();
    private ConnectionListener listener;

    public Server(Storage storage, int port) {
        this.storage = storage;
        this.port = port;
    }

    public synchronized void start() {
        var listener = new ConnectionListener();
        listener.start();
        this.listener = listener;
    }

    public synchronized void stop() throws InterruptedException {
        if (listener != null) {
            IOUtils.closeQuietly(listener);
            listener = null;
        }

        // Выполняем копию, т.к. каждый остановленный поток автоматически удаляет себя из списка connections,
        // а мы хотим выполнить join к каждому, чтобы дождаться, пока завершатся все потоки.
        final var copyConnections = new ArrayList<>(connections);
        copyConnections.forEach(IOUtils::closeQuietly);
        for (ConnectionServer con : copyConnections) {
            con.join();
        }
    }

    @Override
    public void close() throws Exception {
        stop();
    }

    private class ConnectionListener extends Thread implements AutoCloseable {
        private ServerSocket ssocket;

        @Override
        public void run() {
            try (ServerSocket ssocket = new ServerSocket(port)) {
                this.ssocket = ssocket;

                while (!isInterrupted()) {
                    final Socket socket = ssocket.accept();

                    final ConnectionServer connectionServer = new ConnectionServer(socket);
                    synchronized (connections) {
                        connections.add(connectionServer);
                    }
                    connectionServer.start();
                }
            } catch (IOException e) {
                // Можем пропустить вывод ошибки в консоль, если мы знаем, что это был останов.
                if (!isInterrupted()) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void close() throws Exception {
            interrupt();
            // Нужно отдельно закрывать сокет, т.к. метод accept() не выбрасывает InterruptedException,
            // а значит поток из него не выйдет по вызову метода interrupt();
            if (ssocket != null) {
                ssocket.close();
            }
        }
    }

    private class ConnectionServer extends Thread implements AutoCloseable {
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
                    } else if (command instanceof FindAll) {
                        final List<Contact> contacts = storage.find();

                        objOut.writeObject(new Success(contacts));
                    } else {
                        objOut.writeObject(new Failure("Unknown command"));
                    }
                }

            } catch (IOException e) {
                System.err.println("Client " + socket.getInetAddress() + " disconnected");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                synchronized (connections) {
                    connections.remove(this);
                }
            }
        }

        @Override
        public void close() throws Exception {
            interrupt();
            // Аналогично с ServerSocket, метод read не завершится по вызову interrupt().
            if (socket != null) {
                socket.close();
            }
        }
    }
}
