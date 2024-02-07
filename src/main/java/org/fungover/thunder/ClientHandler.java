package org.fungover.thunder;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ClientHandler {
    private final List<Socket> clients;
    private final PackageReader packageReader;

    public ClientHandler() {
        clients = Collections.synchronizedList(new ArrayList<>());
        packageReader = new PackageReader();
    }

    public void handleConnections(ServerSocket serverSocket) {
        while (!serverSocket.isClosed()) {
            Socket connection = null;
            try {
                connection = serverSocket.accept();
                addNewConnectedClient(connection);
            } catch (IOException e) {
                e.printStackTrace();
            }
            removeDisconnectedClients();
        }
    }

    private void addNewConnectedClient(Socket connection) {
        if (connection != null) {
            System.out.println("New client: " + connection.getInetAddress().getHostName());
            clients.add(connection);
        }
    }

    public void removeDisconnectedClients() {
        synchronized (clients) {
            Iterator<Socket> iterator = clients.iterator();
            while (iterator.hasNext()) {
                Socket client = iterator.next();
                if (client.isClosed()) {
                    System.out.println("Client disconnected: " + client.getInetAddress().getHostName());
                    iterator.remove();
                }
            }
        }
    }


    public List<Socket> getClients() {
        return clients;
    }

}
