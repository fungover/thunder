package org.fungover.thunder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    private Client client;
    private TopicHandler topicManager;

    @BeforeEach
    public void setUp() {
        topicManager = Mockito.mock(TopicHandler.class);
        client = new Client("TestClient", topicManager);
    }

    @Test
    void givenClientIdIsSetToTestClientThenGetClientIdShouldReturnTestClient() {
        assertEquals("TestClient", client.getClientId());
    }

    @Test
    void givenClientHasYetNotBeenConnectedThenIsConnectedShouldReturnFalse() {
        assertFalse(client.isConnected());
    }

    @Test
    void givenClientIsConnectedThenIsConnectedShouldReturnTrue() {
        client.connect();
        assertTrue(client.isConnected());
    }

    @Test
    void givenClientIsDisconnectedThenIsConnectedShouldReturnFalse() {
        client.disconnect();
        assertFalse(client.isConnected());
    }

    @Test
    void givenClientIsNotConnectedThenSubscribeShouldResultInNoInteractionWithTopicManager() {
        assertFalse(client.isConnected());
        client.subscribe("topic1");
        Mockito.verifyNoInteractions(topicManager);
    }

    @Test
    void givenClientIsConnectedThenSubscribeWithParameterTopic1ShouldResultInSubscribedTopicsContainingTopic1() {
        client.connect();
        client.subscribe("topic1");
        assertTrue(client.getSubscribedTopics().contains("topic1"));
    }

    @Test
    void givenClientIsNotConnectedThenUnsubscribeShouldResultInNoInteractionWithTopicManager() {
        assertFalse(client.isConnected());
        client.unsubscribe("topic1");
        Mockito.verifyNoInteractions(topicManager);
    }

    @Test
    void givenClientIsConnectedThenUnsubscribeWithParameterTopic1ShouldResultInSubscribedTopicsNotContainingTopic1() {
        client.connect();
        client.subscribe("topic1");
        client.unsubscribe("topic1");
        assertFalse(client.getSubscribedTopics().contains("topic1"));
    }

}
