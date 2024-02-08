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
    @DisplayName("Gets assigned to topic after connection and subscription")
    void getsAssignedToTopicAfterConnectionAndSubscription() throws IOException {
        Socket socketMock = mock(Socket.class);
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x10});
        OutputStream outputStream = new ByteArrayOutputStream();
        when(socketMock.getInputStream()).thenReturn(inputStream);
        when(socketMock.getOutputStream()).thenReturn(outputStream);

        assertTrue(packageReader.isValidConnection(socketMock));

        InputStream inp2 = new ByteArrayInputStream(new byte[]{(byte) 0x82, 0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x2F, 0x77, 0x6F, 0x72, 0x6C, 0x64});
        OutputStream out2 = new ByteArrayOutputStream();
        when(socketMock.getInputStream()).thenReturn(inp2);
        when(socketMock.getOutputStream()).thenReturn(out2);

        packageReader.isValidConnection(socketMock);
        assertThat(subscription.getSubscriptions()).hasSize(1);
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
