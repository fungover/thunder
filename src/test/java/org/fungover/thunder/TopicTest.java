package org.fungover.thunder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TopicTest {

    @Test
    @DisplayName("Exact match when given two topics")
    void exactMatchWhenGivenTwoTopics() {
        Topic topic1 = Topic.create("myhome/first_floor/kitchen/temperature",1);
        Topic topic2 = Topic.create("myhome/first_floor/kitchen/temperature",1);

        assertThat(topic1).isEqualTo(topic2);
    }

    @Test
    @DisplayName("Not exact match when given two topics")
    void notExactMatchWhenGivenTwoTopics() {
        Topic topic1 = Topic.create("myhome/first_floor/kitchen/temperature", 1);
        Topic topic2 = Topic.create("myhome/first_floor/kitchen/", 1);

        assertThat(topic1).isNotEqualTo(topic2);
    }

    @Test
    @DisplayName("Creating topic with empty string should throw IllegalArgumentException")
    void creatingTopicWithEmptyStringShouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Topic.create("",1));

        assertThat(exception).hasMessage("Invalid topic: Should contain at least one character");
    }

    @Test
    @DisplayName("Topic can not start with forward slash")
    void topicCanNotStartWithForwardSlash() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Topic.create("/invalid/topic",1));

        assertThat(exception).hasMessage("Invalid topic: Can not begin with '/'");
    }

    @Test
    @DisplayName("Positive match with single level wildcard (+)")
    void positiveMatchWithSingleLevelWildcard() {
        Topic singleLevelWildcardTopic = Topic.create("myhome/groundfloor/+/temperature",1);

        assertThat(singleLevelWildcardTopic.matchesWildcard("myhome/groundfloor/livingroom/temperature")).isTrue();
        assertThat(singleLevelWildcardTopic.matchesWildcard("myhome/groundfloor/kitchen/temperature")).isTrue();
    }

    @Test
    @DisplayName("Negative match with single level wildcard (+)")
    void negativeMatchWithSingleLevelWildcard() {
        Topic singleLevelWildcardTopic = Topic.create("myhome/groundfloor/+/temperature",1);

        assertThat(singleLevelWildcardTopic.matchesWildcard("myhome/groundfloor/kitchen/brightness")).isFalse();
        assertThat(singleLevelWildcardTopic.matchesWildcard("myhome/firstfloor/kitchen/temperature")).isFalse();
        assertThat(singleLevelWildcardTopic.matchesWildcard("myhome/groundfloor/kitchen/fridge/temperature")).isFalse();
    }

    @Test
    @DisplayName("Positive match with multi level wildcard (#)")
    void positiveMatchWithMultiLevelWildcard() {
        Topic multiLevelWildcardTopic = Topic.create("myhome/groundfloor/#",1);

        assertThat(multiLevelWildcardTopic.matchesWildcard("myhome/groundfloor/livingroom/temperature")).isTrue();
        assertThat(multiLevelWildcardTopic.matchesWildcard("myhome/groundfloor/kitchen/temperature")).isTrue();
        assertThat(multiLevelWildcardTopic.matchesWildcard("myhome/groundfloor/kitchen/brightness")).isTrue();
    }

    @Test
    @DisplayName("Negative match with multi level wildcard (#)")
    void negativeMatchWithMultiLevelWildcard() {
        Topic multiLevelWildcardTopic = Topic.create("myhome/groundfloor/#", 1);

        assertThat(multiLevelWildcardTopic.matchesWildcard("myhome/firstfloor/kitchen/temperature")).isFalse();
    }

    @Test
    @DisplayName("Wrong placement for multi level wildcard in topic should throw Exception")
    void wrongPlacementForMultiLevelWildcardInTopic() {
        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class, () -> Topic.create("myhome/#/temperature", 1));
        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, () -> Topic.create("myhome/groundfloor/#/kitchen/#", 1));

        assertThat(exception1).hasMessage("Invalid topic: Topic name does not follow MQTT topic naming conventions");
        assertThat(exception2).hasMessage("Invalid topic: Topic name does not follow MQTT topic naming conventions");
    }

    @Test
    @DisplayName("No match when levels differ")
    void noMatchWhenWildcardTopicLevelsDiffer() {
        Topic multiLevelWildcardTopic = Topic.create("myhome/groundfloor/#", 1);

        assertThat(multiLevelWildcardTopic.matchesWildcard("myhome/groundfloor")).isFalse();
    }

    @Test
    @DisplayName("creating a special character '$' topic should throw exception")
    void creatingASpecialCharacterTopicShouldThrowException() {
        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class, () -> Topic.create("$SYS",1));
        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, () -> Topic.create("myHome/groundfloor/kitchen/fridge/$ensor",1));

        assertThat(exception1).hasMessage("Invalid topic: Can not contain '$'");
        assertThat(exception2).hasMessage("Invalid topic: Can not contain '$'");
    }

    @Test
    @DisplayName("Topic that is valid for publishing")
    void topicThatIsValidForPublishing() {
        Topic topic1 = Topic.create("myHome/groundfloor/kitchen/temp", 1);

        assertThat(topic1.isValidForPublishing()).isTrue();
    }

    @Test
    @DisplayName("Wildcard topic not valid for publishing")
    void wildcardTopicNotValidForPublishing() {
        Topic wildcardTopic1 = Topic.create("myHome/+/kitchen/#",1);
        Topic wildcardTopic2 = Topic.create("myHome/groundfloor/+",1);
        Topic wildcardTopic3 = Topic.create("myHome/groundfloor/#", 1);

        assertThat(wildcardTopic1.isValidForPublishing()).isFalse();
        assertThat(wildcardTopic2.isValidForPublishing()).isFalse();
        assertThat(wildcardTopic3.isValidForPublishing()).isFalse();
    }

    @Test
    @DisplayName("Internal Topic not valid for publishing")
    void internalTopicNotValidForPublishing() {
        Topic internalTopic1 = new Topic("$SYS",1);
        Topic internalTopic2 = new Topic("$SYS/#",1);

        assertThat(internalTopic1.isValidForPublishing()).isFalse();
        assertThat(internalTopic2.isValidForPublishing()).isFalse();
    }

    @Test
    @DisplayName("Valid topics for subscription")
    void topicValidForSubscription() {
        Topic wildcardTopic1 = Topic.create("myHome/+/kitchen/#",1);
        Topic wildcardTopic2 = Topic.create("myHome/groundfloor/+",1);
        Topic wildcardTopic3 = Topic.create("myHome/groundfloor/#",1);
        Topic wildcardTopic4 = Topic.create("myHome/groundfloor/kitchen/temp",1);

        assertThat(wildcardTopic1.isValidForSubscription()).isTrue();
        assertThat(wildcardTopic2.isValidForSubscription()).isTrue();
        assertThat(wildcardTopic3.isValidForSubscription()).isTrue();
        assertThat(wildcardTopic4.isValidForSubscription()).isTrue();
    }

    @Test
    @DisplayName("Valid internal topic for subscription")
    void validInternalTopicForSubscription() {
        Topic internalTopic = new Topic("$SYS",1);

        assertThat(internalTopic.isValidForSubscription()).isTrue();
    }

    @Test
    @DisplayName("Not valid internal topic for subscription")
    void notValidInternalTopicForSubscription() {
        Topic internalTopic = new Topic("$SYS/#",1);

        assertThat(internalTopic.isValidForSubscription()).isFalse();
    }
}
