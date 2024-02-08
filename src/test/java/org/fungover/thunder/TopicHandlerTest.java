package org.fungover.thunder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TopicHandlerTest {

    private TopicHandler topicHandler;
    private Client client;

    @BeforeEach
    void setUp() {
        topicHandler = new TopicHandler();
        client = new Client("testClient", topicHandler);
    }

    @Test
    void givenClientHasSubscribedToTopicThenSubscribersShouldContainClient() {
        topicHandler.subscribe(client, "topic1");
        Set<Client> subscribers = topicHandler.getSubscriptions().get("topic1");
        assertNotNull(subscribers);
        assertTrue(subscribers.contains(client));
        assertEquals(1, subscribers.size());
    }

    @Test
    void givenClientHasUnsubscribedFromTopicThenSubscribersShouldNotContainClient() {
        topicHandler.subscribe(client, "topic1");
        topicHandler.unsubscribe(client, "topic1");
        Set<Client> subscribers = topicHandler.getSubscriptions().get("topic1");
        assertNull(subscribers);
    }

    @Test
    void givenClientTriesToUnsubscribeFromNonexistentTopicThenUnsubscribeShouldReturnNull() {
        topicHandler.unsubscribe(client, "nonexistentTopic");
        assertNull(topicHandler.getSubscriptions().get("nonexistentTopic"));
    }

}
