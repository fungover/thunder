package org.fungover;

import java.util.Objects;

public class Topic {
    private String name;

    private Topic (String name) {
        this.name = name;
   }

   public String getName() {
        return name;
   }

    public static Topic create(String topicName) {
        if (topicName == null || topicName.isEmpty())
            throw new IllegalArgumentException("Invalid topic: Should contain at least one character");
        if (topicName.startsWith("/"))
            throw new IllegalArgumentException("Invalid topic: Can not begin with '/'");
        if (topicName.contains("#")) {
            int count = countAmountOfMultiLevelWildcard(topicName);
            if (count == 1 && !topicName.endsWith("/#") || count > 1)
                throw new IllegalArgumentException("Invalid topic: Multi-level wildcard (#) must be placed as the last character in the topic, preceded by a forward slash");
        }
        return new Topic(topicName);
    }

    // Checks how many '#' there is in topic
    public static int countAmountOfMultiLevelWildcard(String topicName) {
        int count = 0;
        for (int i = 0; i < topicName.length(); i++) {
            if (topicName.charAt(i) == '#') {
                count++;
            }
        }
        return count;
    }

    public boolean matchesWildcard(String otherTopic) {
        String[] thisLevels = this.name.split("/");
        String[] otherLevels = otherTopic.split("/");

        if (thisLevels.length > otherLevels.length) {
            return false;
        }

        for (int i = 0; i < thisLevels.length; i++) {
            if (!thisLevels[i].equals(otherLevels[i]) && !thisLevels[i].equals("+") && !thisLevels[i].equals("#")) {
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