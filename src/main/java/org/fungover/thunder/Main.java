package org.fungover.thunder;

import java.io.IOException;

public class Main {

    public static void main(String[] args){
        Broker server = null;
        try {
            server = new Broker();
        }  catch (IOException e) {
            e.printStackTrace();
        }
        if (server != null)
            server.start();
    }
}
