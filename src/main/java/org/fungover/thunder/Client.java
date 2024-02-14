package org.fungover.thunder;

import java.util.HashSet;
import java.util.Set;

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

    public void connect() {
        if (!connected) {
            // Establish a connection to the MQTT server
            // Perform the MQTT connection handshake
            System.out.println("Client " + clientId + " connected to server.");
            connected = true;
        } else {
            System.out.println("Client " + clientId + " is already connected.");
        }
    }

    public void disconnect() {
        connected = false;
    }

    public Set<String> getSubscribedTopics() {
        return new HashSet<>(subscribedTopics);
    }



}
