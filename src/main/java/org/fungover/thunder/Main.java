package org.fungover.thunder;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {

    public static void main(String[] args) throws IOException {
        Broker broker = null;
        try {
            ServerSocket serverSocket = new ServerSocket(1883);
            ClientHandler clientHandler = new ClientHandler();
            broker = new Broker(clientHandler, serverSocket);
        }  catch (IOException e) {
        }
        if (broker != null) {
            broker.start();
        }
    }
}
