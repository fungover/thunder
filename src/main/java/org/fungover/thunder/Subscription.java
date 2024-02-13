package org.fungover.thunder;

import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Subscription {
    private final Map<Topic, List<Socket>> subscriptions;

    Subscription() {
        this.subscriptions = new HashMap<>();
    }

    public void read(int length, byte[] buffer, Socket socket) {

        byte[] topicFilter  = Arrays.copyOfRange(buffer, 6, length-1);

            List<Topic> matchingTopics = new ArrayList<>();
            Topic topic = Topic.create(new String(topicFilter, StandardCharsets.UTF_8), buffer[length-1]);

            subscriptions.forEach((topicName, socketList) -> {
                if (topic.matchesWildcard(topic.name())) {
                    socketList.add(socket);
                    matchingTopics.add(topicName);
                }
            });

            if (topic.isValidForSubscription() && matchingTopics.isEmpty()) {
                List<Socket> sockets = new ArrayList<>();
                sockets.add(socket);
                subscriptions.put(topic, sockets);
            }
    }

    public Map<Topic, List<Socket>> getSubscriptions() {
        return subscriptions;
    }
}
