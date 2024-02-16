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

    //Method here for now for testing purpose. Should be deleted when
    public void subscribeToTopic(String topic) {
        subscribedTopics.add(topic);
    }

    public Set<String> getSubscribedTopics() {
        return new HashSet<>(subscribedTopics);
    }
}
