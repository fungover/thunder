package org.fungover.thunder;

import java.util.HashSet;
import java.util.Set;

public class Client {
    private final String clientId;
    private final TopicManager topicManager;
    private boolean connected;
    private Set<String> subscribedTopics;

    public Client(String clientId, TopicManager topicManager) {
        this.clientId = clientId;
        this.topicManager = topicManager;
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
            System.out.println("Client " + clientId + " connected.");
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
            topicManager.subscribe(this, topic);
            subscribedTopics.add(topic);
        }
    }

    public void unsubscribe(String topic) {
        if (connected) {
            topicManager.unsubscribe(this, topic);
            subscribedTopics.remove(topic);
        }
    }

    public void handleMessage(String topic, String message) {
        System.out.println("Received message on topic " + topic + " from client " + clientId + ": " + message);
    }
}
