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

    public void handleConnections(ServerSocket serverSocket) throws IOException {
        while (!serverSocket.isClosed()) {
            Socket connection = null;
            try {
                connection = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (connection != null) {
                if (packageReader.isValidConnection(connection)) {
                    clients.add(connection);
                }else{
                    connection.close();
                }
            }
            removeDisconnectedClients();
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
