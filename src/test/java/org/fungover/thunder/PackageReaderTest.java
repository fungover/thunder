package org.fungover.thunder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PackageReaderTest {
    PackageReader packageReader;

    @BeforeEach
    void setUp() {
        packageReader = new PackageReader();
    }

    @Test
    @DisplayName("Should return true when client send connect package")
    void shouldReturnTrueWhenClientSendConnectPackage() throws IOException {
        Socket socketMock = mock(Socket.class);
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x10, 0x01, 0x00});
        OutputStream outputStream = new ByteArrayOutputStream();
        when(socketMock.getInputStream()).thenReturn(inputStream);
        when(socketMock.getOutputStream()).thenReturn(outputStream);

        assertTrue(packageReader.isValidConnection(socketMock));
    }

    @Test
    @DisplayName("Should return false when sending two connect packages")
    void shouldReturnFalseWhenSendingTwoConnectPackages() throws IOException {
        Socket socketMock = mock(Socket.class);
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x10});
        OutputStream outputStream = new ByteArrayOutputStream();
        when(socketMock.getInputStream()).thenReturn(inputStream);
        when(socketMock.getOutputStream()).thenReturn(outputStream);

        assertTrue(packageReader.isValidConnection(socketMock));
        assertFalse(packageReader.isValidConnection(socketMock));
    }

    @Test
    @DisplayName("Should return false if first package is no connect package")
    void shouldReturnFalseIfFirstPackageIsNoConnectPackage() throws IOException {
        Socket socketMock = mock(Socket.class);
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x20});
        OutputStream outputStream = new ByteArrayOutputStream();
        when(socketMock.getInputStream()).thenReturn(inputStream);
        when(socketMock.getOutputStream()).thenReturn(outputStream);

        assertFalse(packageReader.isValidConnection(socketMock));
    }

    @Test
    @DisplayName("Socket receive response if connect message is sent")
    void socketReceiveResponseIfConnectMessageIsSent() throws IOException {
        Socket socketMock = mock(Socket.class);
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x10});
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(socketMock.getInputStream()).thenReturn(inputStream);
        when(socketMock.getOutputStream()).thenReturn(outputStream);
        packageReader.isValidConnection(socketMock);
        byte[] bytesWritten = outputStream.toByteArray();
        byte[] connackMessage = new byte[]{(byte) 0x20, (byte) 0x02, (byte) 0x00, (byte) 0x00};

        assertArrayEquals(connackMessage, bytesWritten);
    }


    @Test
    @DisplayName("Socket receive no response if no valid connect message is sent")
    void socketReceiveNoResponseIfNoValidConnectMessageIsSent() throws IOException {
        Socket socketMock = mock(Socket.class);
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x20});
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(socketMock.getInputStream()).thenReturn(inputStream);
        packageReader.isValidConnection(socketMock);
        byte[] bytesWritten = outputStream.toByteArray();

        assertEquals(0, bytesWritten.length);
    }
}