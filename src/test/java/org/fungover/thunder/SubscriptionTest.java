package org.fungover.thunder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.Socket;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
            new byte[]{(byte) 0x82, 0x6C, 0x6C, 0x6F, 0x2F, 0x77, 0x6F, 0x72, 0x6C, 0x64,0x01},
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
}
