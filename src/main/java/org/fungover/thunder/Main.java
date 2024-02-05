package org.fungover.thunder;

import java.io.IOException;

public class Main {

    public static void main(String[] args){
        Server server = null;
        try {
            server = new Server();
        }  catch (IOException e) {
            e.printStackTrace();
        }
        if (server != null)
            server.start();
    }
}
