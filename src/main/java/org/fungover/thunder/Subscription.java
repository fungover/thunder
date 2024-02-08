package org.fungover.thunder;

import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Subscription {
    private Map<String, List<Socket>> subscriptions;

    Subscription(){
        this.subscriptions = new HashMap<>();
    }

    public void read(int length,byte[] buffer, Socket socket) {
        byte[] copy = Arrays.copyOfRange(buffer,1,length);
        String topicFilter = new String(copy, StandardCharsets.UTF_8);
        addToSubscription(topicFilter, socket);
        getSubscriptions();
    }

    public void addToSubscription(String topicFilter, Socket socket) {
        List<Socket> sockets = new ArrayList<>();
        if (subscriptions.containsKey(topicFilter)) {
            var list = subscriptions.get(topicFilter);
            list.add(socket);
        } else {
            sockets.add(socket);
            Topic topic = new Topic(topicFilter);
            subscriptions.put(topic.getName(), sockets);
        }
    }

    public Map<String, List<Socket>> getSubscriptions() {
        for (Map.Entry<String, List<Socket>> entry : subscriptions.entrySet()) {
            String topic = entry.getKey();
            List<Socket> sockets = entry.getValue();

            System.out.println("Topic: " + topic);
            System.out.println("Sockets:");
            for (Socket socket : sockets) {
                System.out.println(socket);
            }
        }
        return subscriptions;
    }

}
