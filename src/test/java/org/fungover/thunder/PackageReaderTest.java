package org.fungover.thunder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import static org.assertj.core.api.Assertions.assertThat;
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
    @Test
    @DisplayName("Should return false if double connect message is received")
    void shouldReturnFalseIfDoubleConnectMessageIsReceived() throws IOException {
        Socket socketMock = mock(Socket.class);
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x10, 0x01, 0x00});
        OutputStream outputStream = new ByteArrayOutputStream();
        when(socketMock.getInputStream()).thenReturn(inputStream);
        when(socketMock.getOutputStream()).thenReturn(outputStream);

        assertTrue(packageReader.isValidConnection(socketMock));
        assertFalse(packageReader.isValidConnection(socketMock));
    }

    @Test
    @DisplayName("Should return false if client is still connected")
    void shouldReturnFalseIfClientIsStillConnescted() throws IOException {
        PackageReader packageReader = new PackageReader();
        Socket socketMock = mock(Socket.class);
        InetAddress address = InetAddress.getByName("localhost");
        when(socketMock.getInetAddress()).thenReturn(address);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0xE0});
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(socketMock.getInputStream()).thenReturn(inputStream);
        when(socketMock.getOutputStream()).thenReturn(outputStream);
        assertFalse(packageReader.isCleanDisconnect(address, new byte[]{(byte) 0xE0}, 1));
    }

    @Test
    @DisplayName("Should return false if double connect message is received")
    void shouldReturnFalseIfDoubleConnectMessageIsReceived() throws IOException {
        Socket socketMock = mock(Socket.class);
        InetAddress address = InetAddress.getByName("localhost");

        packageReader.connectPackageSent.put(address, true);

        InputStream inputStream1 = new ByteArrayInputStream(new byte[]{0x10, 0x01, 0x00});
        OutputStream outputStream1 = new ByteArrayOutputStream();
        when(socketMock.getInputStream()).thenReturn(inputStream1);
        when(socketMock.getOutputStream()).thenReturn(outputStream1);

        assertTrue(packageReader.isValidConnection(socketMock));

        InputStream inputStream2 = new ByteArrayInputStream(new byte[]{0x10, 0x01, 0x00});
        OutputStream outputStream2 = new ByteArrayOutputStream();
        when(socketMock.getInputStream()).thenReturn(inputStream2);
        when(socketMock.getOutputStream()).thenReturn(outputStream2);

        assertFalse(packageReader.isValidConnection(socketMock));
    }

    @Test
    @DisplayName("Should return false if first package is not a connect package")
    void shouldReturnFalseIfFirstPackageIsNotConnectPackage() throws IOException {
        Socket socketMock = mock(Socket.class);
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x20});
        OutputStream outputStream = new ByteArrayOutputStream();
        when(socketMock.getInputStream()).thenReturn(inputStream);
        when(socketMock.getOutputStream()).thenReturn(outputStream);

        assertFalse(packageReader.isValidConnection(socketMock));
    }

    @Test
    @DisplayName("Should return false if package is not a connect package")
    void shouldReturnFalseIfPackageIsNotConnectPackage() throws IOException {
        Socket socketMock = mock(Socket.class);
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x05});
        OutputStream outputStream = new ByteArrayOutputStream();
        when(socketMock.getInputStream()).thenReturn(inputStream);
        when(socketMock.getOutputStream()).thenReturn(outputStream);

        assertFalse(packageReader.isValidConnection(socketMock));
    }

}