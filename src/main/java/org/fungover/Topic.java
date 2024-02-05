package org.fungover;

import java.util.Objects;

public class Topic {
    String name;

    private Topic (String name) {
        this.name = name;
   }

    public static Topic create(String topicName) {
        if(topicName == null || topicName.isEmpty() || topicName.startsWith("/"))
            throw new IllegalArgumentException("Invalid topic");
        return new Topic(topicName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Topic topic)) return false;
        return Objects.equals(name, topic.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}