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
        byte[] copy = Arrays.copyOfRange(buffer, 1, length-2);

        //todo första 2 bytes är length, sedan kommer topic filter, efter det kommer qos level. byte[] {längd1,längd2,topic filter....QoS level}

        if (buffer[0] == (byte) 0x82) {
            List<Topic> matchingTopics = new ArrayList<>();
            Topic topic = Topic.create(new String(copy, StandardCharsets.UTF_8), buffer[length-1]);

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
    }

    public Map<Topic, List<Socket>> getSubscriptions() {
        return subscriptions;
    }
}
