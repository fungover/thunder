package com.fungover;


import org.fungover.Topic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class TopicTest{

    @Test
    @DisplayName("Exact match when given two topics")
    void exactMatchWhenGivenTwoTopics() {
        Topic topic1 =  Topic.createTopic ("myhome/first_floor/kitchen/temperature");
        Topic topic2 = Topic.createTopic ("myhome/first_floor/kitchen/temperature");

        assertThat(topic1.isExactMatch(topic2)).isTrue();

    }



}
