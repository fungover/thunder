package org.fungover.thunder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class TopicTest {

    @Test
    @DisplayName("Exact match when given two topics")
    void exactMatchWhenGivenTwoTopics() {
        Topic topic1 = Topic.create("myhome/first_floor/kitchen/temperature");
        Topic topic2 = Topic.create("myhome/first_floor/kitchen/temperature");

        assertThat(topic1.equals(topic2)).isTrue();
    }

    @Test
    @DisplayName("Not exact match when given two topics")
    void notExactMatchWhenGivenTwoTopics() {
        Topic topic1 = Topic.create("myhome/first_floor/kitchen/temperature");
        Topic topic2 = Topic.create("myhome/first_floor/kitchen/");

        assertThat(topic1.equals(topic2)).isFalse();
    }

    @Test
    @DisplayName("Creating topic with empty string should throw IllegalArgumentException")
    void creatingTopicWithEmptyStringShouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Topic.create(""));

        assertThat(exception).hasMessage("Invalid topic: Should contain at least one character");
    }

    @Test
    @DisplayName("Topic can not start with forward slash")
    void topicCanNotStartWithForwardSlash() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Topic.create("/invalid/topic"));

        assertThat(exception).hasMessage("Invalid topic: Can not begin with '/'");
    }

    @Test
    @DisplayName("Positive match with single level wildcard (+)")
    void positiveMatchWithSingleLevelWildcard() {
        Topic singleLevelWildcardTopic = Topic.create("myhome/groundfloor/+/temperature");

        assertThat(singleLevelWildcardTopic.matchesWildcard("myhome/groundfloor/livingroom/temperature")).isTrue();
        assertThat(singleLevelWildcardTopic.matchesWildcard("myhome/groundfloor/kitchen/temperature")).isTrue();
    }

    @Test
    @DisplayName("Negative match with single level wildcard (+)")
    void negativeMatchWithSingleLevelWildcard() {
        Topic singleLevelWildcardTopic = Topic.create("myhome/groundfloor/+/temperature");

        assertThat(singleLevelWildcardTopic.matchesWildcard("myhome/groundfloor/kitchen/brightness")).isFalse();
        assertThat(singleLevelWildcardTopic.matchesWildcard("myhome/firstfloor/kitchen/temperature")).isFalse();
        assertThat(singleLevelWildcardTopic.matchesWildcard("myhome/groundfloor/kitchen/fridge/temperature")).isFalse();
    }

    @Test
    @DisplayName("Positive match with multi level wildcard (#)")
    void positiveMatchWithMultiLevelWildcard() {
        Topic multiLevelWildcardTopic = Topic.create("myhome/groundfloor/#");

        assertThat(multiLevelWildcardTopic.matchesWildcard("myhome/groundfloor/livingroom/temperature")).isTrue();
        assertThat(multiLevelWildcardTopic.matchesWildcard("myhome/groundfloor/kitchen/temperature")).isTrue();
        assertThat(multiLevelWildcardTopic.matchesWildcard("myhome/groundfloor/kitchen/brightness")).isTrue();
    }

    @Test
    @DisplayName("Negative match with multi level wildcard (#)")
    void negativeMatchWithMultiLevelWildcard() {
        Topic multiLevelWildcardTopic = Topic.create("myhome/groundfloor/#");

        assertThat(multiLevelWildcardTopic.matchesWildcard("myhome/firstfloor/kitchen/temperature")).isFalse();
    }

    @Test
    @DisplayName("Wrong placement for multi level wildcard in topic should throw Exception")
    void wrongPlacementForMultiLevelWildcardInTopic() {
        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class, () -> Topic.create("myhome/#/temperature"));
        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, () -> Topic.create("myhome/groundfloor/#/kitchen/#"));

        assertThat(exception1).hasMessage("Invalid topic: Topic name does not follow MQTT topic naming conventions");
        assertThat(exception2).hasMessage("Invalid topic: Topic name does not follow MQTT topic naming conventions");
    }
}
