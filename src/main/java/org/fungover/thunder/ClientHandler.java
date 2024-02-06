package org.fungover.thunder;

import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientHandler {
    private final List<Socket> clients;

    public ClientHandler() {
        clients = new CopyOnWriteArrayList<>();
    }

    public void handleConnection(Socket clientSocket) {
        try {
                System.out.println("New client: " + clientSocket.getInetAddress().getHostName());
                clients.add(clientSocket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Socket> getClients() {
        return clients;
    }
}
