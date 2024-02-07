package org.fungover.thunder;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ClientHandler {
    private final List<Client> clients;

    public ClientHandler() {
        clients = Collections.synchronizedList(new ArrayList<>());
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
            Client client = new Client("Client-" + clients.size(), new TopicHandler());
            client.connect();
            clients.add(client);
            System.out.println("New client: " + connection.getInetAddress().getHostName());
        }
    }

    public void removeDisconnectedClients() {
        synchronized (clients) {
            Iterator<Client> iterator = clients.iterator();
            while (iterator.hasNext()) {
                Client client = iterator.next();
                if (!client.isConnected()) {
                    System.out.println("Client disconnected: " + client.getClientId());
                    iterator.remove();
                }
            }
        }
    }


    public List<Client> getClients() {
        return new ArrayList<>(clients);
    }

}
