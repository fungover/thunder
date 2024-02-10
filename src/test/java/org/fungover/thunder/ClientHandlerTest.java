package org.fungover.thunder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientHandlerTest {
    ClientHandler clientHandler;
    PackageReader packageReader;

    @BeforeEach
    void setUp(){
        clientHandler = new ClientHandler();
        packageReader = mock(PackageReader.class);
    }

    @Test
    @DisplayName("Return 0 if valid client is added and then disconnected")
    void return0IfValidClientIsAddedAndThenDisconnected() throws IOException {
        Socket socketMock = mock(Socket.class);
        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 1883);
        when(socketMock.getInetAddress()).thenReturn(inetSocketAddress.getAddress());
        InputStream connectPacket = new ByteArrayInputStream(new byte[]{0x10});
        InputStream disconnectPacket = new ByteArrayInputStream(new byte[]{(byte) 0xE0});
        OutputStream out = new ByteArrayOutputStream();
        when(socketMock.getOutputStream()).thenReturn(out);
        when(socketMock.getInputStream()).thenReturn(connectPacket,disconnectPacket);

        clientHandler.handleConnection(socketMock);

        assertEquals(0,clientHandler.getClients().size());
    }
    
    @Test
    @DisplayName("Return 0 if not valid client connects")
    void return0IfNotValidClientConnects() throws IOException {
        Socket socketMock = mock(Socket.class);
        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 1883);
        when(socketMock.getInetAddress()).thenReturn(inetSocketAddress.getAddress());
        InputStream in = new ByteArrayInputStream(new byte[]{0x20});
        when(socketMock.getInputStream()).thenReturn(in);

        clientHandler.handleConnection(socketMock);

        assertEquals(0,clientHandler.getClients().size());
    }
}
