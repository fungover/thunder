package org.fungover.thunder;

import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientHandler {
    private final List<Socket> clients;
    private final PackageReader packageReader;

    public ClientHandler() {
        clients = new CopyOnWriteArrayList<>();
        packageReader = new PackageReader();
    }

    public void handleConnection(Socket clientSocket) {
        try {
            while (!clientSocket.isClosed()) {
                System.out.println("New client: " + clientSocket.getInetAddress().getHostName());
                if (packageReader.isValidConnection(clientSocket)) {
                    clients.add(clientSocket);
                } else {
                    clientSocket.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<Socket> getClients() {
        return clients;
    }
}
