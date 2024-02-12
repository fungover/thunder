package org.fungover.thunder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PackageReaderTest {
    PackageReader packageReader;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream mockOut = new ByteArrayOutputStream();

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
    @DisplayName("Should print message and return false when no MQTT CONNECT message is sent")
    void shouldPrintMessageAndReturnFalseWhenNoConnectMessageIsSent() throws IOException {
        Socket socketMock = mock(Socket.class);
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x20});
        when(socketMock.getInputStream()).thenReturn(inputStream);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        when(socketMock.getInputStream()).thenReturn(inputStream);

        assertFalse(packageReader.isValidConnection(socketMock));

        assertThat(outContent.toString()).contains("Received no MQTT CONNECT message. Disconnecting client");
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
    void shouldReturnTrueWhenClientSendDisconnectPackage() throws IOException{
        Socket socketMock = mock(Socket.class);
        setUpConnection(socketMock);
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0xE0,0x00});
        when(socketMock.getInputStream()).thenReturn(inputStream);

        assertThat(packageReader.isCleanDisconnect(socketMock)).isTrue();
    }
    @Test
    @DisplayName("Should return false if there was no connection prior to disconnect")
    void shouldReturnFalseIfThereWasNoConnectionPriorToDisconnect() throws IOException {
        Socket socketMock = mock(Socket.class);
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0xE0,0x00});
        when(socketMock.getInputStream()).thenReturn(inputStream);

        assertThat(packageReader.isCleanDisconnect(socketMock)).isFalse();
    }
    @Test
    @DisplayName("Should return false if isCleanDisconnect() is called without disconnectPackage")
    void shouldReturnFalseIfIsCleanDisconnectIsCalledWithoutDisconnectPackage() throws IOException {
        Socket socketMock = mock(Socket.class);
        setUpConnection(socketMock);

        assertThat(packageReader.isCleanDisconnect(socketMock)).isFalse();
    }
    void setUpConnection(Socket socketMock) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x10, 0x01, 0x00});
        OutputStream outputStream = new ByteArrayOutputStream();
        when(socketMock.getInputStream()).thenReturn(inputStream);
        when(socketMock.getOutputStream()).thenReturn(outputStream);

        packageReader.isValidConnection(socketMock);
    }
}
