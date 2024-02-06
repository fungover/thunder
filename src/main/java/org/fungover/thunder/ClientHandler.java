package org.fungover.thunder;

import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientHandler implements Runnable {
    private final List<Socket> clients;

    public ClientHandler() {
        clients = new CopyOnWriteArrayList<>();
    }

    public void handleConnection(Socket clientSocket){
        try {
            while (!clientSocket.isClosed()) {
                System.out.println("New client: " + clientSocket.getInetAddress().getHostName());
                clients.add(clientSocket);
                clientSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Socket> getClients() {
        return clients;
    }

    @Override
    public void run() {
        //Overridden from Runnable
    }
}
