package org.fungover.thunder;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientHandler {
    private final List<Socket> clients;
    public ClientHandler() {
         clients = Collections.synchronizedList(new ArrayList<>());
    }
    public void handleConnections(ServerSocket serverSocket){
        while(!serverSocket.isClosed()){
            Socket connection = null;
            try {
                connection = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (connection != null) {
                System.out.println("New client: " + connection.getInetAddress().getHostName());
                clients.add(connection);
            }
        }
    }
    public List<Socket> getClients() {
        return clients;
    }

}
