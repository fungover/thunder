package org.fungover.thunder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SubscriptionTest {

    private PackageReader packageReader;
    private Subscription subscription;

    @BeforeEach
    void setUp() {
        packageReader = new PackageReader();
        subscription = new Subscription();
    }


    @Test
    @DisplayName("Two different topic subscriptions should return map size 2")
    void twoDifferentTopicSubscriptionsShouldReturnMapSize2(){
        String topicHelloSlashWorldInBytes = new String(new byte[]{0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x2F, 0x77, 0x6F, 0x72, 0x6C, 0x64},StandardCharsets.UTF_8);
        String topicWorldInBytes = new String(new byte[]{0x57, 0x6F, 0x72, 0x6C, 0x64},StandardCharsets.UTF_8);

        Socket socketMock = mock(Socket.class);
        subscription.addToSubscription(topicHelloSlashWorldInBytes,socketMock);

        Socket socketMockTwo = mock(Socket.class);
        subscription.addToSubscription(topicWorldInBytes, socketMockTwo);

        Socket socketMockThree = mock(Socket.class);
        subscription.addToSubscription(topicWorldInBytes, socketMockThree);

        assertThat(subscription.getSubscriptions()).hasSize(2);
    }


    @Test
    @DisplayName("Return 2 when 2 different topics are added")
    void return2When2DifferentTopicsAreAdded(){
        Socket socket = mock(Socket.class);
        String str = new String(new byte[]{ 0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x2F, 0x77, 0x6F, 0x72, 0x6C, 0x64}, StandardCharsets.UTF_8);

        Socket socket2 = mock(Socket.class);
        String str2 = new String(new byte[]{ 0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x2F, 0x77, 0x6F, 0x72, 0x6C, 0x64}, StandardCharsets.UTF_8);

        Socket socket3 = mock(Socket.class);
        String str3 = new String(new byte[]{ 0x42, 0x65, 0x6C, 0x6C, 0x6F, 0x2F, 0x77, 0x6F, 0x72, 0x6C, 0x64}, StandardCharsets.UTF_8);

        subscription.addToSubscription(str,socket);
        subscription.addToSubscription(str2,socket2);
        subscription.addToSubscription(str3,socket3);

        assertThat(subscription.getSubscriptions()).hasSize(2);
    }
}
