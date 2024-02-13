package org.fungover.thunder;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1883);
        ClientHandler clientHandler = new ClientHandler();
        Broker broker = new Broker(clientHandler, serverSocket);
        broker.start()
    }

}
