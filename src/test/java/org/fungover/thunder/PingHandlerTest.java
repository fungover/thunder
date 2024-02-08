package org.fungover.thunder;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import static org.junit.jupiter.api.Assertions.*;

class PingHandlerTest {

    @Test
    void testIsPingRequest() throws IOException {
        Socket clientSocket = Mockito.mock(Socket.class);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0xC0});
        Mockito.when(clientSocket.getInputStream()).thenReturn(inputStream);

        assertTrue(PingHandler.isPingRequest(clientSocket));
    }

    @Test
    void testIsNotPingRequest() throws IOException {
        Socket clientSocket = Mockito.mock(Socket.class);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x00});
        Mockito.when(clientSocket.getInputStream()).thenReturn(inputStream);

        assertFalse(PingHandler.isPingRequest(clientSocket));
    }

    @Test
    void testSendPingResponse() throws IOException {
        Socket clientSocket = Mockito.mock(Socket.class);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Mockito.when(clientSocket.getOutputStream()).thenReturn(outputStream);

        assertTrue(PingHandler.sendPingResponse(clientSocket));

        assertArrayEquals(new byte[]{(byte) 0xD0, (byte) 0x00}, outputStream.toByteArray());
    }

    @Test
    void testSendPingResponseIOException() throws IOException {
        Socket clientSocket = Mockito.mock(Socket.class);

        Mockito.when(clientSocket.getOutputStream()).thenThrow(IOException.class);

        assertFalse(PingHandler.sendPingResponse(clientSocket));
    }
}
