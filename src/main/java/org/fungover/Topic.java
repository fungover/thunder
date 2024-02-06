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

    public boolean matchesWildcard(String otherTopic) {
        String[] thisLevels = this.name.split("/");
        String[] otherLevels = otherTopic.split("/");

        if (thisLevels.length != otherLevels.length) {
            return false;
        }

        for (int i = 0; i < thisLevels.length; i++) {
            if (!thisLevels[i].equals(otherLevels[i]) && !thisLevels[i].equals("+")) {
                return false;
            }
        }

        return true;
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