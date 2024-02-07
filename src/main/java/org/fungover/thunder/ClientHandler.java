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
            if (packageReader.isValidConnection(clientSocket)) {
                System.out.println("New client: " + clientSocket.getInetAddress().getHostName());
                clients.add(clientSocket);
            } else {
                //Disconnect client/close socket
            }
            while (!clientSocket.isClosed()) {
                //Logic to read from or disconnect client.
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<Socket> getClients() {
        return clients;
    }
}
