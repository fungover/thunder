package org.fungover.thunder;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Server server;
        try {
            server = new Server();
            server.start();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}
