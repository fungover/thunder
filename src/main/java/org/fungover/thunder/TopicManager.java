package org.fungover.thunder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TopicManager {

    private final Map<String, Set<Client>> subscriptions;

    public TopicManager() {
        this.subscriptions = new HashMap<>();
    }

    public void subscribe(Client client, String topic) {
        subscriptions.computeIfAbsent(topic, k -> new HashSet<>()).add(client);
        System.out.println("Client " + client.getClientId() + " subscribed to topic: " + topic);
    }

    public void unsubscribe(Client client, String topic) {
        subscriptions.computeIfPresent(topic, (k, clients) -> {
            clients.remove(client);
            if (clients.isEmpty()) {
                // Remove the topic entry if no clients are subscribed
                return null;
            }
            return clients;
        });
        System.out.println("Client " + client.getClientId() + " unsubscribed from topic: " + topic);
    }

    public Set<Client> getClientsForTopic(String topic) {
        return subscriptions.getOrDefault(topic, new HashSet<>());
    }

}
