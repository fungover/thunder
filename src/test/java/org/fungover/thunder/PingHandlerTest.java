package org.fungover.thunder;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import static org.junit.jupiter.api.Assertions.*;

class PingHandlerTest {

    @Test
    void testIsPingRequest() throws IOException {
        assertTrue(PingHandler.isPingRequest(new byte[]{(byte) 0xC0},1));
    }

    @Test
    void testIsNotPingRequest() throws IOException {
        assertFalse(PingHandler.isPingRequest(new byte[]{(byte) 0x00},1));
    }

    @Test
    void testSendPingResponse() throws IOException {
        Socket clientSocket = Mockito.mock(Socket.class);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Mockito.when(clientSocket.getOutputStream()).thenReturn(outputStream);

        assertTrue(PingHandler.sendPingResponse(outputStream));

        assertArrayEquals(new byte[]{(byte) 0xD0, (byte) 0x00}, outputStream.toByteArray());
    }

    @Test
    void testSendPingResponseIOException() throws IOException {
        OutputStream mockOutputStream = Mockito.mock(OutputStream.class);
        Mockito.doThrow(IOException.class).when(mockOutputStream).write(Mockito.any(byte[].class));
        assertFalse(PingHandler.sendPingResponse(mockOutputStream));
    }
}
