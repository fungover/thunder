package org.fungover.thunder;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import static org.junit.jupiter.api.Assertions.*;

class PingHandlerTest {

    @Test
    void testIsPingRequest() throws IOException {
        byte[] buffer = {(byte) 0xC0};
        assertTrue(PingHandler.isPingRequest(buffer, buffer.length));
    }

    @Test
    void testIsNotPingRequest() throws IOException {
        byte[] buffer = {0x00};
        assertFalse(PingHandler.isPingRequest(buffer, buffer.length));
    }

    @Test
    void testSendPingResponse() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        assertTrue(PingHandler.sendPingResponse(outputStream));

        assertArrayEquals(new byte[]{(byte) 0xD0, (byte) 0x00}, outputStream.toByteArray());
    }

    @Test
    void testSendPingResponseIOException() throws IOException {
        ByteArrayOutputStream outputStream = Mockito.mock(ByteArrayOutputStream.class);

        Mockito.doThrow(IOException.class).when(outputStream).write(Mockito.any(byte[].class));

        assertFalse(PingHandler.sendPingResponse(outputStream));
    }
}
