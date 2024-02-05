package org.fungover;

public class Topic {

    String name;

    public Topic(String name) {
        this.name = name;
    }

    public static Topic createTopic(String topicName) {
        if(topicName == null || topicName.isEmpty())
            throw new IllegalArgumentException("Invalid topic, empty String");
        return new Topic(topicName);
    }


    public boolean isExactMatch(Topic other) {
        return this.name.equals(other.name);
    }
}
