package org.fungover.thunder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.mockito.Mockito.*;

class BrokerTest {
    private Broker broker;
    @Mock
    private ClientHandler clientHandlerMock;
    @Mock
    private Socket clientSocketMock;
    @Mock
    private ServerSocket serverSocketMock;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        broker = new Broker(clientHandlerMock, serverSocketMock);
    }

    @Test
    void givenStartMethodIsCalledWhenServerSocketThrowsExceptionThenExceptionIsCaught() throws IOException {
        when(serverSocketMock.accept()).thenThrow(new IOException("Test Exception"));
        broker.start();
        verify(clientHandlerMock, never()).handleConnection(any(Socket.class));
    }

    @Test
    void givenStartMethodIsCalledWhenConnectionIsNullThenHandleConnectionShouldNotBeInvoked() throws IOException {
        when(serverSocketMock.accept()).thenReturn(null);
        broker.start();
        verify(clientHandlerMock, never()).handleConnection(any(Socket.class));
    }

}
