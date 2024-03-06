package org.fungover.thunder;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.fungover.thunder.Main.logger;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

class ServerTest {
    private static Server server;
    @BeforeAll
    static void setUp() {
        try {
            server = new Server();
            ExecutorService executorService = Executors.newFixedThreadPool(5);
            CountDownLatch latch = new CountDownLatch(1);
            executorService.submit(() -> {
                try {
                    server.start();
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Error starting server", e);
                }
            });
            latch.await(2, TimeUnit.SECONDS);
        } catch (IOException | InterruptedException e) {
            logger.log(Level.SEVERE, "Error in setup", e);
        }
    }
    @AfterAll
    static void tearDown() {
        if (server != null) {
            server.stop();
        }
    }
    @Test
    void testMultipleConnections() {
        int numClients = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(numClients);

        for (int i = 0; i < numClients; i++) {
            final int clientId = i;
            executorService.submit(() -> {
                try {
                    Client client = new Client("Client" + clientId);
                    client.connectToServer("localhost", 1883);
                    assertTrue(client.isConnected());
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Client connection failed", e);
                }
            });
        }

        executorService.shutdown();
        while (!executorService.isTerminated()) {
            try {
                executorService.awaitTermination(2, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "Error waiting for termination", e);
            }
        }
    }
}
