package org.fungover.thunder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PackageReaderTest {
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream mockOut = new ByteArrayOutputStream();
    PackageReader packageReader;

    @BeforeEach
    void setUp() {
        packageReader = new PackageReader();
        System.setOut(new PrintStream(mockOut));
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
    @DisplayName("Should return true when client send disconnect package")
    void shouldReturnTrueWhenClientSendDisconnectPackage() throws IOException {
        Socket socketMock = mock(Socket.class);
        setUpConnection(socketMock);
        byte[] packet = new byte[]{(byte) 0xE0, 0x00};

        assertThat(packageReader.isCleanDisconnect(socketMock, packet, 2)).isTrue();
    }

    @Test
    @DisplayName("Should return false if there was no connection prior to disconnect")
    void shouldReturnFalseIfThereWasNoConnectionPriorToDisconnect() throws IOException {
        Socket socketMock = mock(Socket.class);
        byte[] packet = new byte[]{(byte) 0xE0, 0x00};

        assertThat(packageReader.isCleanDisconnect(socketMock, packet, 2)).isFalse();
    }

    @Test
    @DisplayName("Should return false if isCleanDisconnect() is called without disconnectPackage")
    void shouldReturnFalseIfIsCleanDisconnectIsCalledWithoutDisconnectPackage() throws IOException {
        Socket socketMock = mock(Socket.class);
        setUpConnection(socketMock);
        byte[] packet = new byte[]{(byte) 0x10, 0x00};

        assertThat(packageReader.isCleanDisconnect(socketMock, packet, 2)).isFalse();
    }

    @Test
    @DisplayName("Should return true after handling subscription")
    void shouldReturnTrueAfterHandlingSubscription() throws IOException {
        Socket socketMock = mock(Socket.class);
        byte[] randomInput = new byte[]{(byte) 0x82, 0x06, 0x12, 0x15, 0x14, 0x17, 0x30, 0x60, 0x01};
        byte[] randomOutput = new byte[]{(byte) 0x60, 0x00};
        InputStream inputStream = new ByteArrayInputStream(randomInput);
        OutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(randomOutput);
        when(socketMock.getInputStream()).thenReturn(inputStream);
        when(socketMock.getOutputStream()).thenReturn(outputStream);

        assertThat(packageReader.readFromClient(socketMock)).isTrue();
    }

    @Test
    @DisplayName("Should return true if connectPackage is sent a second time")
    void shouldReturnTrueIfConnectPackageIsSentASecondTime() throws IOException {
        Socket socketMock = mock(Socket.class);
        byte[] randomInput = new byte[]{(byte) 0x10, 0x06, 0x12, 0x15, 0x14, 0x17, 0x30, 0x60, 0x01};
        byte[] randomOutput = new byte[]{(byte) 0x60, 0x00};
        InputStream inputStream = new ByteArrayInputStream(randomInput);
        OutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(randomOutput);
        when(socketMock.getInputStream()).thenReturn(inputStream);
        when(socketMock.getOutputStream()).thenReturn(outputStream);

        assertThat(packageReader.readFromClient(socketMock)).isFalse();
    }


    void setUpConnection(Socket socketMock) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x10, 0x01, 0x00});
        OutputStream outputStream = new ByteArrayOutputStream();
        when(socketMock.getInputStream()).thenReturn(inputStream);
        when(socketMock.getOutputStream()).thenReturn(outputStream);

        packageReader.isValidConnection(socketMock);
    }
}
