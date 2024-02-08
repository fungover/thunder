package org.fungover.thunder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BrokerTest {
    private Broker broker;
    private ClientHandler clientHandlerMock;
    private ServerSocket serverSocketMock;

    @BeforeEach
    void setUp() {
        clientHandlerMock = mock(ClientHandler.class);
        serverSocketMock = mock(ServerSocket.class);
        broker = new Broker(clientHandlerMock, serverSocketMock);
    }

    @Test
    void givenStartMethodIsCalledThenHandleConnectionsMethodShouldBeCalledOnce() throws IOException {
        doNothing().when(clientHandlerMock).handleConnections(serverSocketMock);
        broker.start();
        verify(clientHandlerMock, times(1)).handleConnections(serverSocketMock);
    }

}
