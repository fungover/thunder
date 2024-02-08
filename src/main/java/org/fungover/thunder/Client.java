package org.fungover.thunder;

import java.util.HashSet;
import java.util.Set;

public class Client {
    private final String clientId;
    private final TopicHandler topicHandler;
    private boolean connected;
    private Set<String> subscribedTopics;

    public Client(String clientId, TopicHandler topicHandler) {
        this.clientId = clientId;
        this.topicHandler = topicHandler;
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
            // Establish a connection to the MQTT broker
            // Perform the MQTT connection handshake
            System.out.println("Client " + clientId + " connected to broker.");
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

    public void subscribe(String topic) {
        if (connected) {
            topicHandler.subscribe(this, topic);
            subscribedTopics.add(topic);
        }
    }

    public void unsubscribe(String topic) {
        if (connected) {
            topicHandler.unsubscribe(this, topic);
            subscribedTopics.remove(topic);
        }
    }

}
