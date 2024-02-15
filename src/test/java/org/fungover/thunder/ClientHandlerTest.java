package org.fungover.thunder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

class ClientHandlerTest {

    private ClientHandler clientHandler;
    private PackageReader packageReader;
    private Socket socketMock;
    private InetSocketAddress inetSocketAddress;

    @BeforeEach
    void setUp() throws SocketException {
        clientHandler = new ClientHandler();
        packageReader = new PackageReader();
        socketMock = mock(Socket.class);
        inetSocketAddress = new InetSocketAddress("localhost", 1883);
    }

    @Test
    @DisplayName("Return 0 if valid client is added and then disconnected")
    void return0IfValidClientIsAddedAndThenDisconnected() throws IOException {

        when(socketMock.getInetAddress()).thenReturn(inetSocketAddress.getAddress());
        InputStream connectPacket = new ByteArrayInputStream(new byte[]{0x10});
        InputStream disconnectPacket = new ByteArrayInputStream(new byte[]{(byte) 0xE0});
        when(socketMock.getInputStream()).thenReturn(connectPacket, disconnectPacket);

        clientHandler.handleConnection(socketMock);

        assertEquals(0, clientHandler.getClients().size());
    }

    @Test
    @DisplayName("Return 0 if not valid client connects")
    void return0IfNotValidClientConnects() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[]{0x20});
        when(socketMock.getInputStream()).thenReturn(in);

        assertFalse(packageReader.isValidConnection(socketMock));
    }

    @Test
    @DisplayName("Should remove disconnected clients")
    void shouldRemoveDisconnectedClients() throws IOException {
        Socket socket1 = mock(Socket.class);
        Socket socket2 = mock(Socket.class);
        when(socket1.isClosed()).thenReturn(true);
        when(socket2.isClosed()).thenReturn(false);
        when(socket1.getInetAddress()).thenReturn(InetAddress.getByName("localhost"));
        when(socket2.getInetAddress()).thenReturn(InetAddress.getByName("localhost"));

        clientHandler.getClients().addAll(List.of(socket1, socket2));
        clientHandler.removeDisconnectedClients();

        assertEquals(Collections.singletonList(socket2), clientHandler.getClients());
    }
}
