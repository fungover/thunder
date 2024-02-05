package com.fungover;


import org.fungover.Topic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TopicTest{

    @Test
    @DisplayName("Exact match when given two topics")
    void exactMatchWhenGivenTwoTopics() {
        Topic topic1 =  Topic.create ("myhome/first_floor/kitchen/temperature");
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
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,()-> Topic.create(""));

        assertThat(exception).hasMessage("Invalid topic");

    }

    @Test
    @DisplayName("Topic can not start with forward slash")
    void topicCanNotStartWithForwardSlash() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Topic.create("/invalid/topic"));

        assertThat(exception).hasMessage("Invalid topic");
    }

}
