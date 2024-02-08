package org.fungover.thunder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TopicHandlerTest {

    private TopicHandler topicHandler;
    private Client client1;
    private Client client2;

    @BeforeEach
    void setUp() {
        topicHandler = new TopicHandler();
        client1 = new Client("testClient1", topicHandler);
        client2 = new Client("testClient2", topicHandler);
    }

    @Test
    void givenClientHasSubscribedToTopicThenSubscribersShouldContainClient() {
        topicHandler.subscribe(client1, "topic1");
        Set<Client> subscribers = topicHandler.getSubscriptions().get("topic1");
        assertNotNull(subscribers);
        assertTrue(subscribers.contains(client1));
        assertEquals(1, subscribers.size());
    }

    @Test
    void givenClientHasUnsubscribedFromTopicThenSubscribersShouldNotContainClient() {
        topicHandler.subscribe(client1, "topic1");
        topicHandler.unsubscribe(client1, "topic1");
        Set<Client> subscribers = topicHandler.getSubscriptions().get("topic1");
        assertNull(subscribers);
    }

    @Test
    void givenClientTriesToUnsubscribeFromNonexistentTopicThenUnsubscribeShouldReturnNull() {
        topicHandler.unsubscribe(client1, "nonexistentTopic");
        assertNull(topicHandler.getSubscriptions().get("nonexistentTopic"));
    }

    @Test
    void givenClientTriesToUnsubscribeFromTopicTheyHaventSubscribedToThenSubscribersShouldBeNull() {
        topicHandler.unsubscribe(client1, "nonexistentTopic");

        assertNull(topicHandler.getSubscriptions().get("nonexistentTopic"));
    }

    @Test
    void givenClientsExistWhenUnsubscribingThenReturnClients() {
        topicHandler.subscribe(client1, "topic4");
        topicHandler.subscribe(client2, "topic4");

        topicHandler.unsubscribe(client1, "topic4");

        Set<Client> remainingSubscribers = topicHandler.getSubscriptions().get("topic4");
        assertNotNull(remainingSubscribers);
        assertTrue(remainingSubscribers.contains(client2));

        assertEquals(remainingSubscribers, topicHandler.getSubscriptions().computeIfPresent("topic4", (k, clients) -> {
            clients.remove(client1);
            if (clients.isEmpty()) {
                return null;
            }
            return clients;
        }));
    }

}
