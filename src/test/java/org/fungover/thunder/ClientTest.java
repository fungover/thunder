package org.fungover.thunder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    private Client client;

    @BeforeEach
    public void setUp() {
        client = new Client("TestClient");
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
    public void givenClientHasSubscribedToTopicsThenGetSubscribedTopicsShouldReturnASetOfThoseTopics() {
        String clientId = "testClient";
        Client client = new Client(clientId);

        client.subscribeToTopic("topic1");
        client.subscribeToTopic("topic2");

        assertEquals(Set.of("topic1", "topic2"), client.getSubscribedTopics());
    }


    @Test
    public void givenClientHasNotSubscribedToAnyTopicThenGetSubscribedTopicsShouldReturnAnEmptyHashSet() {
        String clientId = "testClient";
        Client client = new Client(clientId);

        assertTrue(client.getSubscribedTopics().isEmpty());
    }

    @Test
    public void givenClientHasSubscribedToATopicTwiceThenGetSubscribedTopicsShouldReturnASetWithOnlyOneOccurrenceOfTheTopic() {
        String clientId = "testClient";
        Client client = new Client(clientId);

        client.subscribeToTopic("topic1");
        client.subscribeToTopic("topic1");

        assertEquals(Set.of("topic1"), client.getSubscribedTopics());
    }
}
