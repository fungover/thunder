package org.fungover.thunder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import static org.fungover.thunder.Main.logger;

public class Client {
    private final String clientId;
    private boolean connected;
    private Set<String> subscribedTopics;

    public Client(String clientId) {
        this.clientId = clientId;
        this.connected = false;
        this.subscribedTopics = new HashSet<>();
    }

    public String getClientId() {
        return clientId;
    }

    public boolean isConnected() {
        return connected;
    }

    //Method here for now for testing purpose. Should be deleted when
    public void subscribeToTopic(String topic) {
        subscribedTopics.add(topic);
    }

    public Set<String> getSubscribedTopics() {
        return new HashSet<>(subscribedTopics);
    }

    public void connectToServer(String serverAddress, int serverPort) throws IOException {
        try (Socket socket = new Socket(serverAddress, serverPort);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("Hello from " + clientId);
            String response = (String) in.readObject();

            if ("Connection successful".equals(response)) {
                this.connected = true;
            }
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error during connection", e);
        }
    }
}
