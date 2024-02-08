package org.fungover.thunder;

public record Topic(String name) {


    public static Topic create(String topicName) {
        checkTopicNamingValidation(topicName);
        return new Topic(topicName);
    }

    private static void checkTopicNamingValidation(String topicName) {
        if (topicName == null || topicName.isEmpty())
            throw new IllegalArgumentException("Invalid topic: Should contain at least one character");

        if (topicName.startsWith("/"))
            throw new IllegalArgumentException("Invalid topic: Can not begin with '/'");

        if (topicName.contains("$"))
            throw new IllegalArgumentException("Invalid topic: Can not contain '$'");

        if (!isAValidTopicName(topicName))
            throw new IllegalArgumentException("Invalid topic: Topic name does not follow MQTT topic naming conventions");
    }

    //checks a created topic name if its valid
    public static boolean isAValidTopicName(String topicName) {
        long count = 0L;
        int numInPart = 0;

        if (topicName.contains("#")) {
            count = topicName
                .chars()
                .filter(c -> c == '#')
                .count();
        }

        String[] parts = topicName.split("/");

        for (String part : parts) {
            numInPart = part.length();
            //check every part for invalid wildcards and if it's empty
            if (part.isEmpty() || (numInPart > 1 && part.contains("+")) || (((count == 1) && !topicName.endsWith("/#")) || (count > 1))) {
                return false;
            }
        }

        return true;
    }

    // check two topic names to match wildcard
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

    public boolean isValidForPublishing() {
        return !(name.startsWith("$") || name.endsWith("/#") || name.contains("/+"));
    }

}
