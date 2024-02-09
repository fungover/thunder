package org.fungover.thunder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class PackageReader {
    private final Map<InetAddress, Boolean> connectPackageSent = new HashMap<>();
    private final Subscription subscription = new Subscription();
    private static final int CONNECTION_TIMEOUT = 30000;

    public boolean isValidConnection(Socket socket) throws IOException {
        InetAddress client = socket.getInetAddress();
        byte[] buffer = new byte[1024];
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        socket.setSoTimeout(CONNECTION_TIMEOUT);
        int bytesRead = inputStream.read(buffer);

        if (isDoubleConnectMessage(client, buffer)) {
            return false;
        }

        if (isConnectPackage(bytesRead, buffer)) {
            System.out.println("Received MQTT CONNECT message from client");
            connectPackageSent.put(socket.getInetAddress(), true);
            sendConnackToClient(outputStream);
            System.out.println("Sent MQTT CONNACK message to client");
            return true;
        }

        return false;
    }

    private boolean isDoubleConnectMessage(InetAddress client, byte[] buffer) {
        return connectPackageSent.containsKey(client) && Boolean.TRUE.equals(connectPackageSent.get(client)) && buffer[0] == 0x10;
    }

    private static boolean isConnectPackage(int bytesRead, byte[] buffer) {
        return bytesRead > 0 && buffer[0] == (byte) 0x10;
    }

    public boolean readFromClient(Socket clientSocket) throws IOException {
        byte[] buffer = new byte[1024];
        InputStream inputStream = clientSocket.getInputStream();
        OutputStream outputStream = clientSocket.getOutputStream();
        clientSocket.setSoTimeout(CONNECTION_TIMEOUT);
        int bytesRead = inputStream.read(buffer);

        if (buffer[0] == 0x10) {
            System.out.println("Sending connection package after connection is established is forbidden.");
            return false;
        }

        if (buffer[0] == (byte) 0x82) {
           subscription.read(bytesRead, buffer, clientSocket);

            sendSubackToClient(outputStream);
        }

        return false;
    }

    private static void sendConnackToClient(OutputStream outputStream) throws IOException {
        byte[] connackMessage = new byte[]{(byte) 0x20, (byte) 0x02, (byte) 0x00, (byte) 0x00};
        outputStream.write(connackMessage);
        outputStream.flush();
    }

    private static void sendSubackToClient(OutputStream outputStream) throws IOException {
        byte[] subackMessage = new byte[]{(byte) 0x20, (byte) 0x02, (byte) 0x00, (byte) 0x00};
        outputStream.write(subackMessage);
        outputStream.flush();
    }

    private static boolean isDisconnectPackage(int bytesRead, byte[] buffer){
        return bytesRead > 0 && buffer[0] == (byte) 0xE0;
    }

    public boolean isCleanDisconnect(Socket socket) throws IOException {
        InetAddress client = socket.getInetAddress();

        if (isClientConnected(client)){
            return false;
        }

        InputStream inputStream = socket.getInputStream();
        byte[] buffer = new byte[1024];
        int bytesRead = inputStream.read(buffer);

        if (isDisconnectPackage(bytesRead, buffer)) {
            System.out.println("Received MQTT DISCONNECT message from client");
            connectPackageSent.remove(client);
            return true;
        }
        return false;
    }

    private boolean isClientConnected(InetAddress client) {
        return !connectPackageSent.containsKey(client);
    }
}
