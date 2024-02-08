package org.fungover.thunder;

import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Subscription {
    private Map<String, List<Socket>> subscriptions;

    Subscription() {
        this.subscriptions = new HashMap<>();
    }

    public void read(int length, byte[] buffer, Socket socket) {
        byte[] copy = Arrays.copyOfRange(buffer, 1, length);
        String byteToTopicString = new String(copy, StandardCharsets.UTF_8);
        addToSubscription(byteToTopicString, socket);
        getSubscriptions();
    }

    public boolean addToSubscription(String topicFilter, Socket socket) {
        List<Socket> sockets = new ArrayList<>();
        if (subscriptions.containsKey(topicFilter)) {
            subscriptions.get(topicFilter).add(socket);
            return true;
        } else if (!subscriptions.containsKey(topicFilter)) {
            sockets.add(socket);
            Topic topic = new Topic(topicFilter);
            subscriptions.put(topic.getName(), sockets);
            return true;
        }
        return false;
    }

    public Map<String, List<Socket>> getSubscriptions() {
        for (Map.Entry<String, List<Socket>> entry : subscriptions.entrySet()) {
            String topic = entry.getKey();
            List<Socket> sockets = entry.getValue();

            System.out.println("Topic: " + topic);
            System.out.println("Sockets subscribing on topic:");
            for (Socket socket : sockets) {
                System.out.println(socket);
            }
            System.out.println("---------------------");
        }
        return subscriptions;
    }

}
