package org.fungover.thunder;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerTest {
    private static Server server;
    @BeforeAll
    public static void setUp() {
        try {
            server = new Server();
            ExecutorService executorService = Executors.newFixedThreadPool(5);
            executorService.submit(() -> {
                try {
                    server.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            Thread.sleep(1000);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    @AfterAll
    public static void tearDown() {
        if (server != null) {
            server.stop();
        }
    }
    @Test
    public void testMultipleConnections() {
        int numClients = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(numClients);

        for (int i = 0; i < numClients; i++) {
            final int clientId = i;
            executorService.submit(() -> {
                try {
                    Client client = new Client("Client" + clientId);
                    client.connectToServer("localhost", 1883);
                    assertTrue(client.isConnected());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
