package org.fungover.thunder;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {

    public static void main(String[] args){
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(1883);
            System.out.println("Server started on port 1883");
        }  catch (IOException e) {
            e.printStackTrace();
        }
        ClientHandler clientHandler = new ClientHandler();
        if (serverSocket != null)
            clientHandler.handleConnections(serverSocket);
    }
}
