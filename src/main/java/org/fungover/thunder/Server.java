package org.fungover.thunder;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final ServerSocket serverSocket;
    private final ClientHandler clientHandler;

    public Server() throws IOException {
        this.clientHandler = new ClientHandler();
        this.serverSocket = new ServerSocket(1883);
    }

    public void start() throws IOException {
        System.out.println("Server started on port 1883");
        try {
            while (!serverSocket.isClosed()) {
                Socket connection = serverSocket.accept();
                if (connection == null)
                    throw new NullPointerException("Connection is null");
                Thread.ofVirtual().start(() -> clientHandler.handleConnection(connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
