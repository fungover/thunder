package org.fungover.thunder;

import java.io.IOException;
import java.net.ServerSocket;

public class Broker {

    private final ServerSocket serverSocket;
    private final ClientHandler clientHandler;
    public Broker() throws IOException {
        this.clientHandler = new ClientHandler();
        this.serverSocket = new ServerSocket(1883);
    }

    public void start() {
        try {
            System.out.println("Server started on port 1883");
            clientHandler.handleConnections(serverSocket);
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
}
