package org.fungover.thunder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClientHandlerTest {
    ClientHandler clientHandler;
    PackageReader packageReader;

    @BeforeEach
    void setUp(){
        clientHandler = new ClientHandler();
        packageReader = mock(PackageReader.class);
    }

    @Test
    @DisplayName("Return 1 if valid client is added")
    void return1IfValidClientIsAdded() throws IOException {
        Socket socketMock = mock(Socket.class);
        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 1883);
        when(socketMock.getInetAddress()).thenReturn(inetSocketAddress.getAddress());
        InputStream in = new ByteArrayInputStream(new byte[]{0x10});
        OutputStream out = new ByteArrayOutputStream();
        when(socketMock.getOutputStream()).thenReturn(out);
        when(socketMock.getInputStream()).thenReturn(in);
        when(packageReader.isValidConnection(socketMock)).thenReturn(true);
        clientHandler.handleConnection(socketMock);

        assertEquals(1,clientHandler.getClients().size());
    }

    @Test
    @DisplayName("Return 0 if not valid client connects")
    void return0IfNotValitClientConnects() throws IOException {
        Socket socketMock = mock(Socket.class);
        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 1883);
        when(socketMock.getInetAddress()).thenReturn(inetSocketAddress.getAddress());
        InputStream in = new ByteArrayInputStream(new byte[]{0x20});
        OutputStream out = new ByteArrayOutputStream();
        when(socketMock.getOutputStream()).thenReturn(out);
        when(socketMock.getInputStream()).thenReturn(in);
        when(packageReader.isValidConnection(socketMock)).thenReturn(true);
        clientHandler.handleConnection(socketMock);

        assertEquals(0,clientHandler.getClients().size());
    }
}
