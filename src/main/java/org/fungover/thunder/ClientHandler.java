package org.fungover.thunder;


import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ClientHandler {
    private final List<Socket> clients;
    private final PackageReader packageReader;
    boolean isClientSocketClosed = false;

    public ClientHandler() {
        clients = Collections.synchronizedList(new ArrayList<>());
        packageReader = new PackageReader();
    }

    public void handleConnection(Socket clientSocket) {
        try {
            if (packageReader.isValidConnection(clientSocket)) {
                System.out.println("New client: " + clientSocket.getInetAddress().getHostName());
                clients.add(clientSocket);
            } else {
                clientSocket.close();
            }
            while (!isClientSocketClosed) {
                isClientSocketClosed = packageReader.readFromClient(clientSocket);
            }
            closeClientSocket(clientSocket);
            removeDisconnectedClients();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeClientSocket(Socket clientSocket) throws IOException {
        clientSocket.close();
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
