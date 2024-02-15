package org.fungover.thunder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class SubscriptionTest {

    private Subscription subscription;
    private Socket socketMock;

    @BeforeEach
    void setUp() {
        subscription = new Subscription();
        socketMock = mock(Socket.class);
    }

    @Test
    @DisplayName("Return subscription size 1 when read method is processed")
    void returnSubscriptionSize1WhenReadMethodIsProcessed() {
        subscription.read(11,
            new byte[]{(byte) 0x82, 0x6C, 0x6C, 0x6F, 0x2F, 0x77, 0x6F, 0x72, 0x6C, 0x64, 0x01},
            socketMock);

        assertThat(subscription.getSubscriptions()).hasSize(1);
    }

    @Test
    @DisplayName("Throw exception when topic start with /")
    void throwExceptionWhenTopicStartWith() {
        assertThrows(IllegalArgumentException.class, () ->
            subscription.read(6,
                new byte[]{(byte) 0x82, 0x2F, 0x77, 0x6F, 0x72, 0x6C, 0x64},
                socketMock));
    }

    @Test
    @DisplayName("Should subscribe to all wildcardmatched topics")
    void shouldSubscribeToAllWildcardmatchedTopics() {
        Socket socketMockTwo = mock(Socket.class);
        Socket socketMockThree = mock(Socket.class);
        byte[] headerAndLengthFill = new byte[]{0x10, 0x20, 0x30, 0x40, 0x50, 0x70};
        byte[] qos = new byte[]{0x01};

        byte[] fullTopic = "House/Livingroom/Light".getBytes(StandardCharsets.UTF_8);
        int totalLength = headerAndLengthFill.length + fullTopic.length;
        byte[] concatenatedBytes = new byte[totalLength + 1];
        System.arraycopy(headerAndLengthFill, 0, concatenatedBytes, 0, headerAndLengthFill.length);
        System.arraycopy(fullTopic, 0, concatenatedBytes, headerAndLengthFill.length, fullTopic.length);
        System.arraycopy(qos, 0, concatenatedBytes, concatenatedBytes.length - 1, qos.length);

        subscription.read(concatenatedBytes.length, concatenatedBytes, socketMockTwo);

        byte[] fullTopic3 = "House/Bedroom/Light".getBytes(StandardCharsets.UTF_8);
        int totalLength3 = headerAndLengthFill.length + fullTopic3.length;
        byte[] concatenatedBytes3 = new byte[totalLength3 + 1];
        System.arraycopy(headerAndLengthFill, 0, concatenatedBytes3, 0, headerAndLengthFill.length);
        System.arraycopy(fullTopic3, 0, concatenatedBytes3, headerAndLengthFill.length, fullTopic3.length);
        System.arraycopy(qos, 0, concatenatedBytes3, concatenatedBytes3.length - 1, qos.length);

        subscription.read(concatenatedBytes3.length, concatenatedBytes3, socketMockThree);
        assertThat(subscription.getSubscriptions()).hasSize(2);

        byte[] wildcardTopic = "House/+/Light".getBytes(StandardCharsets.UTF_8);
        int totalLengthTwo = headerAndLengthFill.length + wildcardTopic.length;
        byte[] concatenatedBytesTwo = new byte[totalLengthTwo + 1];
        System.arraycopy(headerAndLengthFill, 0, concatenatedBytesTwo, 0, headerAndLengthFill.length);
        System.arraycopy(wildcardTopic, 0, concatenatedBytesTwo, headerAndLengthFill.length, wildcardTopic.length);
        System.arraycopy(qos, 0, concatenatedBytesTwo, concatenatedBytesTwo.length - 1, qos.length);

        subscription.read(concatenatedBytesTwo.length, concatenatedBytesTwo, socketMock);

        Topic checkTopic = new Topic("House/Bedroom/Light", 1);
        Topic checkTopicTwo = new Topic("House/Livingroom/Light", 1);
        assertThat(subscription.getSubscriptions()).hasSize(2);
        assertThat(subscription.getSubscriptions().get(checkTopic)).contains(socketMock);
        assertThat(subscription.getSubscriptions().get(checkTopicTwo)).contains(socketMock);
    }
}
